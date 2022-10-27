package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = findOrderTables(tableGroup);
        tableGroup.validateExistOrderTable(savedOrderTables.size());

        final TableGroup newTableGroup = new TableGroup(null, LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(newTableGroup);

        final List<OrderTable> groupedOrderTables = groupOrderTable(savedOrderTables, savedTableGroup.getId());

        savedTableGroup.updateOrderTables(groupedOrderTables);
        return savedTableGroup;
    }

    private List<OrderTable> findOrderTables(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private List<OrderTable> groupOrderTable(final List<OrderTable> savedOrderTables, final Long tableGroupId) {
        final List<OrderTable> newOrderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupTableBy(tableGroupId);
            newOrderTables.add(orderTableDao.save(savedOrderTable));
        }
        return newOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateOrderTableNotInCompletion(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderTableNotInCompletion(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
