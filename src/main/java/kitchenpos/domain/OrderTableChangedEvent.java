package kitchenpos.domain;

public class OrderTableChangedEvent {

    private final Long orderTableId;

    public OrderTableChangedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
