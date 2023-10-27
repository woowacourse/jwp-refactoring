package kitchenpos.domain.order;

import java.util.List;

public class Orders {

    private final List<Order> orders;

    public Orders(final List<Order> orders) {
        this.orders = orders;
    }

    public boolean containsNotCompleteOrder() {
        return orders.stream()
                     .anyMatch(Order::isNotComplete);
    }
}
