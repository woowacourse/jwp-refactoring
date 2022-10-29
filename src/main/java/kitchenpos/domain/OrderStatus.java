package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(it -> it.name().equals(name))
                .findAny()
                .orElse(null);
    }

    public boolean isCooking() {
        return this.equals(COOKING);
    }

    public boolean isMeal() {
        return this.equals(MEAL);
    }
}
