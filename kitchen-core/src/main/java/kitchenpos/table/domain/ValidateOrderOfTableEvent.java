package kitchenpos.table.domain;

public class ValidateOrderOfTableEvent {
    private Long orderTableId;

    public ValidateOrderOfTableEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
