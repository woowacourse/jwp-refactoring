package kitchenpos.domain;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    COOKING("COOKING"),
    MEAL("MEAL"),
    COMPLETION("COMPLETION");

    private static final Map<String, OrderStatus> ORDER_STATUS_MAP = new HashMap<>();

    static {
        for (OrderStatus status : values()) {
            ORDER_STATUS_MAP.put(status.name, status);
        }
    }

    private final String name;

    OrderStatus(final String name) {
        this.name = name;
    }

    public static OrderStatus from(final String orderStatus) {
        return ORDER_STATUS_MAP.get(orderStatus);
    }

    public String getName() {
        return name;
    }
}
