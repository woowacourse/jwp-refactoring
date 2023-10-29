package kitchenpos.ordertable.domain;

public class OrderTableChangedEmptyEvent {

    private Long orderTableId;

    public OrderTableChangedEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
