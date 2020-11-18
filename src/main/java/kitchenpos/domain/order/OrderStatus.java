package kitchenpos.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus findOrderStatus(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> status.equals(orderStatus.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 상태를 찾을 수 없습니다."));
    }
}
