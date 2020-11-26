package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean canUngroup() {
        return this != COOKING && this != MEAL;
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
