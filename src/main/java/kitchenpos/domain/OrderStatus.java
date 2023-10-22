package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus get(final String name) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
