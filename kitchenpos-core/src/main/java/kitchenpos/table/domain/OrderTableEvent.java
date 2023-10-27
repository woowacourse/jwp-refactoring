package kitchenpos.table.domain;

public final class OrderTableEvent {

    private final Long orderTableId;

    public OrderTableEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
