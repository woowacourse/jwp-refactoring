package kitchenpos.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String name) {
        return Arrays.stream(OrderStatus.values())
                .filter(it -> it.name().equals(name))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean isCooking() {
        return this.equals(COOKING);
    }

    public boolean isMeal() {
        return this.equals(MEAL);
    }

    public boolean isCompletion() {
        return this.equals(COMPLETION);
    }
}
