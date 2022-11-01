package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(value -> value.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
