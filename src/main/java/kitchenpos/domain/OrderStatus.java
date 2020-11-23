package kitchenpos.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    boolean isCompleted() {
        return this == COMPLETION;
    }
}
