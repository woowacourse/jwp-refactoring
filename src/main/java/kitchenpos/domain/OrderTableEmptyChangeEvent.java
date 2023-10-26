package kitchenpos.domain;

public class OrderTableEmptyChangeEvent {
    private final Long orderTableId;

    public OrderTableEmptyChangeEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
