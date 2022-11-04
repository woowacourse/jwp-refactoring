package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.exception.notfound.OrderStatusNotFoundException;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION,
    ;

    public static OrderStatus from(final String value) {
        return Arrays.stream(values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new OrderStatusNotFoundException(value));
    }
}
