package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.exception.InvalidOrderStatusException;

public class Orders {

    private final List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public void validateCompleted() {
        orders.stream()
            .filter(Order::isNotCompletion)
            .findAny()
            .ifPresent(order -> {
                throw new InvalidOrderStatusException(String.format("%s ID의 Order가 요리 중이거나 식사 중 입니다.", order.getId()));
            });
    }
}
