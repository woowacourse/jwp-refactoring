package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<String> collectInProgress() {
        return Arrays.asList(COOKING.name(), MEAL.name());
    }

    public static boolean isCompleted(OrderStatus orderStatus) {
        return Objects.equals(COMPLETION, orderStatus);
    }
}
