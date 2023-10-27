package kitchenpos.domain.order;

public class OrderedEvent {

    private final Long orderTableId;

    public OrderedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long orderTableId() {
        return orderTableId;
    }
}
