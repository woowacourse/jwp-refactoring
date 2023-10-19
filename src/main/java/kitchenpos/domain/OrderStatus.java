package kitchenpos.domain;

import java.util.List;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION
    ;

    public static List<String> notCompletion() {
        return List.of(COOKING.name(), MEAL.name());
    }
}
