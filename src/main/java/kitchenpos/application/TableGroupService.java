package kitchenpos.application;

import java.time.LocalDateTime;
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
import kitchenpos.dto.TableGroupRequest;
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
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = getValidateOrderTables(tableGroupRequest);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.setTableGroupIdInOrderTables(savedTableGroup.getId());
        for (final OrderTable savedOrderTable : savedOrderTables) {
            orderTableDao.save(new OrderTable(savedTableGroup.getId(),
                    savedOrderTable.getTableGroupId(),
                    savedOrderTable.getNumberOfGuests(),
                    savedOrderTable.isEmpty()));
        }
        return tableGroupDao.findById(savedTableGroup.getId()).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        final List<Long> orderTableIds = tableGroup.getTableIds();
        if (isStatusCookingOrMeal(orderTableIds)) {
            throw new IllegalArgumentException();
        }

        tableGroup.unGrouping();
        for (final OrderTable orderTable : tableGroup.getOrderTables()) {
            orderTableDao.save(orderTable);
        }
    }

    private List<OrderTable> getValidateOrderTables(TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = tableGroupRequest.getOrderTables();

        if (isUnderTwoOrderTable(orderTables)) {
            throw new IllegalArgumentException();
        }
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
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

    private boolean isStatusCookingOrMeal(List<Long> orderTableIds) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
