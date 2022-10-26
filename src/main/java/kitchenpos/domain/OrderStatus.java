package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatus {

    COOKING,
    MEAL,
    COMPLETION

    ;

    public static List<OrderStatus> listInProgress() {
        return Arrays.stream(values())
            .filter(status -> status != COMPLETION)
            .collect(Collectors.toList());
    }

    public boolean isCompleted() {
        return this == COMPLETION;
    }
}
