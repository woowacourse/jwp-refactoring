package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean matches(final OrderStatus other) {
        return this == other;
    }
}
