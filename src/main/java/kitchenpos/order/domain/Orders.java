package kitchenpos.order.domain;

import java.util.List;

public class Orders {

    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void checkAllOrderCompleted() {
        for (Order order : orders) {
            checkOrderIsCompleted(order);
        }
    }

    private void checkOrderIsCompleted(Order order) {
        if (order.isCompleted()) {
            return;
        }
        throw new IllegalArgumentException();
    }
}
