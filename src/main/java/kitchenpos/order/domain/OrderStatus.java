package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isCompletion() {
        return this.equals(COMPLETION);
    }

    public boolean isNotCompletion() {
        return !this.equals(COMPLETION);
    }
}
