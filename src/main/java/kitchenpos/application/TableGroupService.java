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
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
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
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = new OrderTables(toOrderTables(tableGroupRequest));
        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        validateOrderTablesSize(orderTables, savedOrderTables);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        return updateOrderTableIdAndTableGroupId(savedOrderTables, savedTableGroup);
    }

    private List<OrderTable> toOrderTables(TableGroupRequest tableGroupRequest) {
        return tableGroupRequest.getOrderTables()
                .stream()
                .map(it -> new OrderTable(it.getId()))
                .collect(Collectors.toList());
    }

    private TableGroup updateOrderTableIdAndTableGroupId(List<OrderTable> savedOrderTables,
                                                         TableGroup savedTableGroup) {
        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);
        return savedTableGroup;
    }

    private void validateOrderTablesSize(final OrderTables orderTables, final List<OrderTable> savedOrderTables) {
        orderTables.validateIsSameSize(savedOrderTables);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
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
