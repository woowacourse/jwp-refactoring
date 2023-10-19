package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroup);

        return saveTableGroup(tableGroup, savedOrderTables);
    }

    private List<OrderTable> getOrderTables(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        validateOrderTablesSize(orderTables);

        return getOrderTables(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> getOrderTables(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        validateMatchingSizes(orderTables, savedOrderTables);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            validateTableGroup(savedOrderTable);
        }
        return savedOrderTables;
    }

    private void validateTableGroup(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMatchingSizes(final List<OrderTable> orderTables, final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup saveTableGroup(final TableGroup tableGroup, final List<OrderTable> savedOrderTables) {
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.attachTableGroup(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatus(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.detachTableGroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )) {
            throw new IllegalArgumentException();
        }
    }
}
