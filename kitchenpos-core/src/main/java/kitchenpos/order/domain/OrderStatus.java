package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {

    COOKING,
    MEAL,
    COMPLETION;

    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(orderStatus))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 주문 상태입니다. order status: " + orderStatus));
    }
}
