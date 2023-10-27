package kitchenpos.event;

public class ValidateOrderTableIsNotEmptyEvent {

    private final Long orderTableId;

    public ValidateOrderTableIsNotEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
