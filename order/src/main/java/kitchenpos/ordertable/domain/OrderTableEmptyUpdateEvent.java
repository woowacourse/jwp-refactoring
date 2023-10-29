package kitchenpos.ordertable.domain;

public class OrderTableEmptyUpdateEvent {
    private final Long orderTableId;

    public OrderTableEmptyUpdateEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
