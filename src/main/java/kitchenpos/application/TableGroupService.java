package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.response.OrderTableIdResponse;
import kitchenpos.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(List<Long> orderTableIds) {
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        final List<OrderTable> orderTables = findOrderTablesIdIn(orderTableIds);

        tableGroup.union(orderTables);

        for (OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable);
        }

        return tableGroup;
    }

    private List<OrderTable> findOrderTablesIdIn(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    @Transactional(readOnly = true)
    public TableGroupResponse findById(long tableId) {
        TableGroup tableGroup = tableGroupDao.findById(tableId).orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());

        List<OrderTableIdResponse> orderTableIds = orderTables.stream()
                .map(o -> new OrderTableIdResponse(o.getId()))
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
