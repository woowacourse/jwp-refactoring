package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.table.exception.InvalidFindAllOrderTablesException;

public class OrderTables {

    private static final int MINIMUM_ORDER_TABLES_SIZE = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_TABLES_SIZE) {
            throw new InvalidFindAllOrderTablesException();
        }
    }

    public void joinWithTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.occupyTableGroup(tableGroup.getId());
        }
    }

    public void ungroup(List<Order> orders) {
        validateUngroup(orders);
        for (OrderTable orderTable : this.orderTables) {
            orderTable.unOccupyTableGroup();
        }
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
