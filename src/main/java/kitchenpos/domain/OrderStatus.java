package kitchenpos.domain;

import java.util.Arrays;
import kitchenpos.exception.InvalidOrderStatusException;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus findByString(String orderStatus) {
        return Arrays.stream(values())
                .filter(status -> status.name().equals(orderStatus.toUpperCase()))
                .findFirst()
                .orElseThrow(InvalidOrderStatusException::new);
    }

    public boolean isCompletion() {
        return this.name().equals("COMPLETION");
    }
}
