package kitchenpos.order.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING(1),
    MEAL(2),
    COMPLETION(3),
    ;

    private final int order;

    OrderStatus(int order) {
        this.order = order;
    }

    public static OrderStatus from(String name) {
        return Arrays.stream(values())
                     .filter(it -> it.name().equals(name))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("주문 상태가 존재하지 않습니다."));
    }

    public static OrderStatus nextOf(OrderStatus orderStatus) {
        return Arrays.stream(values())
                     .filter(it -> it.order == orderStatus.order + 1)
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("다음 상태가 존재하지 않습니다."));
    }
}
