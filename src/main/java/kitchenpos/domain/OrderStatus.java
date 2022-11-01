package kitchenpos.domain;

import java.util.Arrays;
import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isCompletion() {
        return Objects.equals(this, COMPLETION);
    }
}
