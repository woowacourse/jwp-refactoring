package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    private static final List<OrderStatus> NOT_COMPLETE_STATUS = Arrays.asList(COOKING, MEAL);

    public static List<OrderStatus> getNotCompleteStatus() {
        return NOT_COMPLETE_STATUS;
    }
}
