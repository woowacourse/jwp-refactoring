package kitchenpos.common.event;

public class OrderCheckEvent {

    private final Long orderTableId;

    public OrderCheckEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
