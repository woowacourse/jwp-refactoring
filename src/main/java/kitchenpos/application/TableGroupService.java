package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroup create(final OrderTables tables) {
        if (!tables.isGroupAble()) {
            throw new IllegalArgumentException();
        }
        final List<Long> orderTableIds = tables.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateOrderTables(tables, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), tables));
        groupTables(tables, savedTableGroup);
        return savedTableGroup;
    }

    private void validateOrderTables(final OrderTables tables, final List<OrderTable> savedOrderTables) {
        if (!tables.hasValidOrderTables(savedOrderTables.size())) {
            throw new IllegalArgumentException();
        }
    }

    private void groupTables(final OrderTables tables, final TableGroup savedTableGroup) {
        final Long tableGroupId = savedTableGroup.getId();
        tables.groupTables(tableGroupId);
        savedTableGroup.changeAllOrderTables(tables);
        for (final OrderTable savedOrderTable : tables.getOrderTables()) {
            orderTableDao.update(
                    new OrderTable(
                            savedOrderTable.getId(),
                            tableGroupId,
                            savedOrderTable.getNumberOfGuests(),
                            savedOrderTable.isEmpty()
                    )
            );
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateTableStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.changeUngroupTable();
            orderTableDao.update(orderTable);
        }

    }

    private void validateTableStatus(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
