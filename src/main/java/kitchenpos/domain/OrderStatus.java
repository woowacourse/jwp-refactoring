package kitchenpos.domain;

import java.util.Objects;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static boolean isCompletion(String status) {
        return Objects.equals(COMPLETION.name(), status);
    }
}
