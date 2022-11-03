package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompleted() {
        return COMPLETION.equals(this);
    }
}
