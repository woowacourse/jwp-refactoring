package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static List<String> getOngoingStatuses() {
        return Arrays.asList(COOKING.name(), MEAL.name());
    }
}
