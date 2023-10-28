package kitchenpos.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus find(final String target) {
        return Arrays.stream(values())
                .filter(status -> status.name().equals(target))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
