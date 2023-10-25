package kitchenpos.common;

public class ValidateOrderStatusEvent {

    private final Long orderTableId;

    public ValidateOrderStatusEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
