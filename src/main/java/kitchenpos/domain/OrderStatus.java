package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public static OrderStatus from(String input) {
        return Arrays.stream(values())
                .filter(orderStatus -> isSameName(orderStatus, input))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static boolean isSameName(OrderStatus orderStatus, String input) {
        return orderStatus.name()
                .equalsIgnoreCase(input);
    }

}
