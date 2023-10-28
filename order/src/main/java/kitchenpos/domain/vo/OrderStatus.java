package kitchenpos.domain.vo;

import java.util.Arrays;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equals(status))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 상태입니다."));
    }
}
