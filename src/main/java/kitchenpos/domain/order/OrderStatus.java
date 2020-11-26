package kitchenpos.domain.order;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatus {
    COOKING("cooking"),
    MEAL("meal"),
    COMPLETION("completion");

    public static final List<OrderStatus> NOT_COMPLETION_ORDER_STATUSES = Arrays
        .stream(OrderStatus.values())
        .filter(orderStatus -> !orderStatus.equals(COMPLETION))
        .collect(Collectors.toList());

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
