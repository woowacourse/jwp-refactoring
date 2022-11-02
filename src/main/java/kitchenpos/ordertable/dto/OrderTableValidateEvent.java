package kitchenpos.ordertable.dto;

public class OrderTableValidateEvent {

    private Long orderTableId;

    public OrderTableValidateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
