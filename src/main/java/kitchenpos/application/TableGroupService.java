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
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public TableGroup create(final TableGroup request) {
        validateRequestOrderTablesSize(request);

        final List<OrderTable> orderTables = findOrderTables(request);
        validateOrderTablesSize(request, orderTables);
        validateHaveNotEmptyOrderTable(orderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.addOrderTables(orderTables);

        updateOrderTables(savedTableGroup);

        return savedTableGroup;
    }

    private void validateRequestOrderTablesSize(final TableGroup tableGroup) {
        if (tableGroup.isInvalidOrderTablesSize()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.generateOrderTableIds();
        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTablesSize(final TableGroup tableGroup, final List<OrderTable> savedOrderTables) {
        if (tableGroup.isSameOrderTablesSize(savedOrderTables.size())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateHaveNotEmptyOrderTable(final List<OrderTable> orderTables) {
        boolean isNotEmptyOrderTable = orderTables.stream()
                .anyMatch(OrderTable::isNotEmpty);

        if (isNotEmptyOrderTable) {
            throw new IllegalArgumentException();
        }
    }

    private void updateOrderTables(final TableGroup tableGroup) {
        final Long tableGroupId = tableGroup.getId();
        List<OrderTable> orderTables = tableGroup.getOrderTables()
                .stream()
                .map(it -> new OrderTable(it.getId(), tableGroupId, it.getNumberOfGuests(), false))
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateCannotUngroupCondition(orderTables);

        for (final OrderTable orderTable : orderTables) {
            OrderTable updatedOrderTable = new OrderTable(orderTable.getId(), orderTable.getNumberOfGuests(), false);
            orderTableDao.save(updatedOrderTable);
        }
    }

    private void validateCannotUngroupCondition(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = extractOrderTableIds(orderTables);

        List<String> conditions = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        boolean haveConditions = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, conditions);

        if (haveConditions) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> extractOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
