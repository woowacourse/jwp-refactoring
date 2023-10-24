package kitchenpos.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus of(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("주문 상태가 존재하지 않습니다."));
    }
}
