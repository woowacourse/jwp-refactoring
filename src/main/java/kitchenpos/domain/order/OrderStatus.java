package kitchenpos.domain.order;

import kitchenpos.exception.OrderStatusNotFoundException;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String orderStatus) {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(() -> new OrderStatusNotFoundException(orderStatus));
    }
}
