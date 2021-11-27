package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isNotCompletion() {
        return this != COMPLETION;
    }

    public boolean isCompletion() {
        return this == COMPLETION;
    }
}
