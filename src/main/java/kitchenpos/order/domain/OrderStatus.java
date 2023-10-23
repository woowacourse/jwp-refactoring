package kitchenpos.order.domain;

public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public boolean isComplete() {
        return this == COMPLETION;
    }

    public OrderStatus next() {
        if (this == COOKING) {
            return MEAL;
        }
        if (this == MEAL) {
            return COMPLETION;
        }
        throw new IllegalArgumentException("주문 상태를 변경할 수 없습니다.");
    }
}
