package kitchenpos.domain;

public enum OrderStatus {

    COOKING,
    MEAL,
    COMPLETION;

    public boolean isCompleted() {
        return this == COMPLETION;
    }

    public boolean isNotCompleted() {
        return this != COMPLETION;
    }
}
