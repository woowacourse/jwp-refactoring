package kitchenpos.domain.order;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean matches(final OrderStatus other) {
        return this == other;
    }
}
