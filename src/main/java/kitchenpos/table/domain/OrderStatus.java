package kitchenpos.table.domain;

import java.util.Arrays;

public enum OrderStatus {

    NO_ORDER,
    COOKING,
    MEAL,
    COMPLETION;

    public boolean isCompleted() {
        return this.equals(COMPLETION);
    }

    public boolean existCustomer() {
        return this.equals(COOKING) || this.equals(MEAL);
    }

    public static OrderStatus from(final String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
