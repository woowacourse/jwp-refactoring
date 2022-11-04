package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    boolean isPossibleNextStatus(final OrderStatus nextStatus) {
        if (this == COOKING) {
            return nextStatus == MEAL;
        }
        if (this == MEAL) {
            return nextStatus == COMPLETION;
        }
        return false;
    }
}
