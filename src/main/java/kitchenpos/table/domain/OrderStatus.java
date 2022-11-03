package kitchenpos.table.domain;

import java.util.Arrays;

public enum OrderStatus {

    COOKING,
    MEAL,
    COMPLETION;

    public boolean isCompleted() {
        return this.equals(COMPLETION);
    }

    public static OrderStatus from(final String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
