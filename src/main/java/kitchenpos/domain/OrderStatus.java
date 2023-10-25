package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<String> notCompleteStatus() {
        return Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
    }
}
