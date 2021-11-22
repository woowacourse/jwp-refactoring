package table.domain;

public class OrdersValidatedEvent {

    private Long orderTableId;

    public OrdersValidatedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
