package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String name) {
        return Arrays.stream(values())
                .filter(orderStatus -> orderStatus.name().equals(name.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 상태입니다."));
    }
}
