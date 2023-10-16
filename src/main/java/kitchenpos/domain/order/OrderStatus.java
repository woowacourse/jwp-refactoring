package kitchenpos.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus of(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
