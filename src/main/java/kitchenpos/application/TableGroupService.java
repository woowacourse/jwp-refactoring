package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        validateTableGroup(tableGroup);
        validateOrderTable(tableGroup);
        return tableGroupDao.save(tableGroup.initCreatedDate());
    }

    private static void validateTableGroup(final TableGroup tableGroup) {
        if (!tableGroup.hasValidOrderTableSize()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        final List<OrderTable> savedOrderTables = getOrderTables(orderTables);
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable savedOrderTable : savedOrderTables) {
            validateOrderTable(savedOrderTable);
        }
    }

    private List<OrderTable> getOrderTables(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private static void validateOrderTable(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || savedOrderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = getOrderTables(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable.ungroup());
        }
    }

    private List<OrderTable> getOrderTables(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }
}
