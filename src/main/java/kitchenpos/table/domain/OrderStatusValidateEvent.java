package kitchenpos.table.domain;

public class OrderStatusValidateEvent {

    private final Long orderTableId;

    public OrderStatusValidateEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
