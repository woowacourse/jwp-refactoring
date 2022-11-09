package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
