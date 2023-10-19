package kitchenpos.domain;

import static java.util.Locale.ENGLISH;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String name) {
        return OrderStatus.valueOf(name.toUpperCase(ENGLISH));
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
