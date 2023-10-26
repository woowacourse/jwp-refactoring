package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(orderStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
