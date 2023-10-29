package kitchenpos.domain;

public class OrderStatusCheckEvent {

    private final Long orderTableId;

    public OrderStatusCheckEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
