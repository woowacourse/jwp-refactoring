package kitchenpos.table.domain;

public class OrderTableOrderedEvent {

    private final Long orderTableId;

    public OrderTableOrderedEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
