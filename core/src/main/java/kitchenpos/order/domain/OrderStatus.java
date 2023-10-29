package kitchenpos.order.domain;

public enum OrderStatus {

    COOKING,
    MEAL,
    COMPLETION,
    ;

    public boolean isInProgress() {
        return this == MEAL || this == COOKING;
    }
}
