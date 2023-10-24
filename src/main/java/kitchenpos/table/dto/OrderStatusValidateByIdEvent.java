package kitchenpos.table.dto;

public class OrderStatusValidateByIdEvent {

    private final Long orderTableId;

    public OrderStatusValidateByIdEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
