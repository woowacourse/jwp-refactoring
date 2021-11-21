package kitchenpos.table.application;

public class OrdersCompletedCheckEvent {
    private final Long orderTableId;

    public OrdersCompletedCheckEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
