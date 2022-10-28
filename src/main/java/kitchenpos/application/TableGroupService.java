package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(
                collectTableIds(request.getOrderTables()));
        compareSize(savedOrderTables, request.getOrderTables());

        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.checkCanGroup();
        final TableGroup savedTableGroup = tableGroupDao.save(TableGroup.ofNew(orderTables));

        final Long tableGroupId = savedTableGroup.getId();
        saveOrderTablesWithTableGroupId(orderTables, tableGroupId);
        savedTableGroup.setOrderTables(orderTables);

        return savedTableGroup;
    }

    private List<Long> collectTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void compareSize(List<OrderTable> savedOrderTables, List<OrderTable> orderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void saveOrderTablesWithTableGroupId(OrderTables orderTables, Long tableGroupId) {
        for (final OrderTable orderTable : orderTables.getValue()) {
            orderTable.setTableGroupId(tableGroupId);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, OrderStatus.collectInProgress())) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
