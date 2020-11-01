package kitchenpos.domain.order;

import java.util.Arrays;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isOrderStatus(String orderStatus) {
        return Arrays.stream(values())
                .anyMatch(name -> name.name().equals(orderStatus));
    }

    public static boolean isCompletion(String orderStatus) {
        if (!isOrderStatus(orderStatus)) {
            return false;
        }
        return OrderStatus.valueOf(orderStatus) == COMPLETION;
    }
}
