package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static void checkIfHas(String orderStatus) {
        if (Arrays.stream(OrderStatus.values())
                .noneMatch(status -> status.name().equals(orderStatus))) {
            throw new OrderStatusException("해당하는 주문 상태가 없습니다.");
        }
    }
}
