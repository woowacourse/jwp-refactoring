package kitchenpos.table.application;

public class CheckAllOrderCompletedEvent {
    private final Long orderTableId;

    public CheckAllOrderCompletedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
