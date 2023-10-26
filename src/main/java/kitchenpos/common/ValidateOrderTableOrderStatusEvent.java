package kitchenpos.common;

public class ValidateOrderTableOrderStatusEvent {

    private final Long orderTableId;

    public ValidateOrderTableOrderStatusEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
