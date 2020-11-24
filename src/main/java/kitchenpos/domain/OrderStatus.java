package kitchenpos.domain;

import java.util.stream.Stream;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static OrderStatus of(String status) {
        return Stream.of(values())
            .filter(orderStatus -> status.equalsIgnoreCase(orderStatus.name()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static boolean isDefinedOrderStatus(String target) {
        try {
            of(target);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
