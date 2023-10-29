package kitchenpos.dto;

public class OrderTableIdValidateEvent {

    private final Long orderTableId;

    public OrderTableIdValidateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
