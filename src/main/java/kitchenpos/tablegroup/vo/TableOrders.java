package kitchenpos.tablegroup.vo;

import java.util.List;
import kitchenpos.order.domain.Order;

public class TableOrders {

    private final List<Order> orders;

    public TableOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean hasCookingOrMealOrder() {
        return orders.stream().anyMatch(Order::isCookingOrMealStatus);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
