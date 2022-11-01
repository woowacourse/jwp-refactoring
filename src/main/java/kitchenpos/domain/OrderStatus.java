package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.common.exception.InvalidOrderException;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus find(String name) {
        return Arrays.stream(values())
                .filter(orderStatus -> name.equals(orderStatus.name()))
                .findFirst()
                .orElseThrow(() -> new InvalidOrderException("유효하지 않은 주문 상태입니다."));
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
