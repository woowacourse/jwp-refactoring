package kitchenpos.domain;

public enum OrderStatus {
    COOKING,
    MEAL,
    COMPLETION;

    public OrderStatus transitionToNextStatus() {
        OrderStatus orderStatus;
        switch (this) {
            case COOKING:
                orderStatus = MEAL;
                break;
            case MEAL:
                orderStatus = COMPLETION;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return orderStatus;
    }
}
