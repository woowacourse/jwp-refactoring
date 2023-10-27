package kitchenpos.common.event;

public class OrderTableChangeEmptyEvent {

    private final Long orderTableId;

    public OrderTableChangeEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
