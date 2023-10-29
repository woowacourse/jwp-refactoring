package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문 상태를 찾을 수 없습니다."));
    }
}
