package kitchenpos.order.domain;

import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(OrderStatus orderStatus) {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
    }
}
