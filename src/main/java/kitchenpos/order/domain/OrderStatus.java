package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean cannotUngroup() {
        return this == COOKING || this == MEAL;
    }
}
