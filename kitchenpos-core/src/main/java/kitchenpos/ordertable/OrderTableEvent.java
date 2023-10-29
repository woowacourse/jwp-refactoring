package kitchenpos.ordertable;

public class OrderTableEvent {
    private final Long orderTableId;

    public OrderTableEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
