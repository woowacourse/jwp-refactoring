package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<String> getUnmodifiableStatus() {
        return Arrays.asList(COOKING.name(), MEAL.name());
    }
}
