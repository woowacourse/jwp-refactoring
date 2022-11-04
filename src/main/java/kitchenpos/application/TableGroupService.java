package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        final OrderTables orderTables = findOrderTables(request.getOrderTables());
        OrderTables groupedOrderTables = orderTables.group(savedTableGroup);
        orderTableDao.saveAll(groupedOrderTables.getValues());
        return savedTableGroup;
    }

    private OrderTables findOrderTables(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTables, savedOrderTables);
        return new OrderTables(savedOrderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables, final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableDao.findAllByTableGroupId(tableGroupId));
        orderTables.ungroup();
        orderTableDao.saveAll(orderTables.getValues());
    }
}
