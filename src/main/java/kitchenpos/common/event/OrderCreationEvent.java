package kitchenpos.common.event;

public class OrderCreationEvent {

    private final Long orderTableId;

    public OrderCreationEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

}


