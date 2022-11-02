package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isAvailable(final String status) {
        return Arrays.stream(OrderStatus.values())
                .anyMatch(i -> i.name().equals(status));
    }
}
