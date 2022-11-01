package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(final String orderStatus) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<String> getCookingAndMealStatusNames() {
        return List.of(COOKING.name(), MEAL.name());
    }

    public boolean isCompletion() {
        return Objects.equals(this, COMPLETION);
    }
}
