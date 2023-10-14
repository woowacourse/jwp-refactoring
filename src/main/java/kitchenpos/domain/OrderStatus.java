package kitchenpos.domain;

import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> notCompletion() {
        return List.of(COOKING, MEAL);
    }
}
