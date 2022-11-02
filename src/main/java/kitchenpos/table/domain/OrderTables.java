package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.table.exception.InvalidGroupOrderTablesSizeException;

public class OrderTables {

    private static final int MINIMUM_ORDER_TABLES_SIZE = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_TABLES_SIZE) {
            throw new InvalidGroupOrderTablesSizeException();
        }
    }

    public OrderTables joinWithTableGroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (OrderTable orderTable : this.orderTables) {
            orderTables.add(orderTable.occupyTableGroup(tableGroup.getId()));
        }
        return new OrderTables(orderTables);
    }

    public OrderTables ungroup(List<Order> orders) {
        validateUngroup(orders);
        List<OrderTable> orderTables = new ArrayList<>();
        for (OrderTable orderTable : this.orderTables) {
            orderTables.add(orderTable.unOccupyTableGroup());
        }
        return new OrderTables(orderTables);
    }

    private void validateUngroup(List<Order> orders) {
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        for (Order order : orders) {
            order.validateUngroup(orderTableIds);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
