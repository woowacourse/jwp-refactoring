package kitchenpos.core.order.domain;

import java.util.Objects;

public enum OrderStatus {
    COMPLETION(null),
    MEAL(COMPLETION),
    COOKING(MEAL);

    private final OrderStatus nextStatus;

    OrderStatus(final OrderStatus nextStatus) {
        this.nextStatus = nextStatus;
    }

    public OrderStatus transitionTo(OrderStatus orderStatus) {
        if (canBeTransitionedTo(orderStatus)) {
            return this.nextStatus;
        }
        throw new IllegalArgumentException();
    }

    private boolean canBeTransitionedTo(final OrderStatus orderStatus) {
        return Objects.equals(this.nextStatus, orderStatus);
    }
}
