package kitchenpos.order.domain;

import java.util.List;

public class Orders {

    private final List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders create(List<Order> orders) {
        return new Orders(orders);
    }

    public void validateCompleted() {
        orders.stream()
                .filter(Order::isNotCompletion)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("orderId : " + order.getId() + "인 Order가 Completion 상태가 아닙니다.");
                });
    }
}
