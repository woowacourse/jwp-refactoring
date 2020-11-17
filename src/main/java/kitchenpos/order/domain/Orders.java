package kitchenpos.order.domain;

import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isUngroupable() {
        return this.orders.stream()
                .allMatch(Order::isUngroupable);
    }
}
