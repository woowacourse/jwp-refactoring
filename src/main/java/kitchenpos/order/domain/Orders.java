package kitchenpos.order.domain;

import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void checkNotCompleted() {
        orders.stream()
                .filter(Order::isNotCompleted)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("아직 조리 혹은 식사 중인 주문이 존재합니다.");
                });
    }
}
