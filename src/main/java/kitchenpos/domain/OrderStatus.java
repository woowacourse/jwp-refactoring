package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String status) {
        return Arrays.stream(values())
            .filter(orderStatus -> orderStatus.name().equals(status))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("제공되지 않는 OrderStatus 입니다."));
    }
}
