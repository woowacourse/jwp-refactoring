package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.exception.OrderStatusNotFoundException;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public static OrderStatus from(String value) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(OrderStatusNotFoundException::new);
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
