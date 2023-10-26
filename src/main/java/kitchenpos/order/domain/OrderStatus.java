package kitchenpos.order.domain;

import kitchenpos.order.exception.NotExistOrderStatusException;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;
    
    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(values())
                     .filter(status -> status.name().equals(orderStatus))
                     .findFirst()
                     .orElseThrow(() -> new NotExistOrderStatusException("존재하지 않는 주문상태 입니다"));
    }
}
