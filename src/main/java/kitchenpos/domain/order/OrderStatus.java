package kitchenpos.domain.order;

import kitchenpos.exception.OrderStatusNotFoundException;
import kitchenpos.util.ValidateUtil;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String orderStatus) {
        ValidateUtil.validateNonNullAndNotEmpty(orderStatus);

        return Arrays.stream(OrderStatus.values())
                .filter(status -> status.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(() -> new OrderStatusNotFoundException(orderStatus));
    }
}
