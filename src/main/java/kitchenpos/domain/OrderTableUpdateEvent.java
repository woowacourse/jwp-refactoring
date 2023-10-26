package kitchenpos.domain;

public class OrderTableUpdateEvent {
    private final Long orderTableId;

    public OrderTableUpdateEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
