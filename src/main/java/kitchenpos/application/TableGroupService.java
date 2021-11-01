package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroup);
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    private List<OrderTable> getOrderTables(TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (isUnderTwoOrderTable(orderTables)) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = tableGroup.getTableIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        checkValidOrderTables(orderTables, savedOrderTables);

        return savedOrderTables;
    }

    private void checkValidOrderTables(List<OrderTable> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean isUnderTwoOrderTable(List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = tableGroup.getTableIds();
        if (isStatusCookingOrMeal(orderTableIds)) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private boolean isStatusCookingOrMeal(List<Long> orderTableIds) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
