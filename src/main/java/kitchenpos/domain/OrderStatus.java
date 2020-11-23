package kitchenpos.domain;

import java.util.stream.Stream;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus of(String status) {
        return Stream.of(values())
            .filter(orderStatus -> status.equalsIgnoreCase(orderStatus.name()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
