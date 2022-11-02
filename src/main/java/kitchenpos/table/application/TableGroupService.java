package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupCreateResponse;
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
    public TableGroupCreateResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final OrderTables orderTables = new OrderTables(tableGroupCreateRequest.getOrderTables());

        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableDao.findAllByIdIn(orderTableIds));
        validateOrderTables(orderTables, savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), orderTables));
        groupTables(orderTables, savedTableGroup);

        return TableGroupCreateResponse.from(savedTableGroup);
    }

    private void validateOrderTables(final OrderTables tables, final OrderTables savedOrderTables) {
        if (!savedOrderTables.isGroupAble()) {
            throw new IllegalArgumentException();
        }
        if (!tables.hasValidOrderTableSize(savedOrderTables.getOrderTables())) {
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
