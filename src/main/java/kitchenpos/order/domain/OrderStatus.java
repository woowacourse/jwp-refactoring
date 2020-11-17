package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean canUngroup() {
        return this != COOKING && this != MEAL;
    }
}
