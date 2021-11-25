package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableGroupValidateImpl implements TableGroupValidator {

    private static final int MINIMUM_NUMBER_OF_GROUP_TABLE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupValidateImpl(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateGrouping(TableGroup tableGroup) {
        OrderTables orderTables = tableGroup.getOrderTables();
        if (orderTables.isEmpty() || isTablesLessThanTwo(tableGroup)) {
            throw new IllegalArgumentException();
        }

        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTables.getOrderTableIds()));

        if (orderTables.isDifferentSize(savedOrderTables)) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables.getValues()) {
            validateGroupingTable(orderTable);
        }
    }

    private boolean isTablesLessThanTwo(TableGroup tableGroup) {
        return tableGroup.getOrderTables().getValues().size() < MINIMUM_NUMBER_OF_GROUP_TABLE;
    }

    private void validateGroupingTable(OrderTable orderTable) {
        if (orderTable.isNotEmpty() || orderTable.isGrouping()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validateUngroup(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables().getOrderTableIds(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
