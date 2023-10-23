package kitchenpos.domain;

import kitchenpos.domain.exception.NotExistOrderStatus;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;
    
    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(values())
                     .filter(status -> status.name().equals(orderStatus))
                     .findFirst()
                     .orElseThrow(() -> new NotExistOrderStatus("존재하지 않는 주문상태 입니다"));
    }
}
