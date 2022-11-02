package kitchenpos.event;

public class VerifiedOrderTableEvent {

    private Long orderTableId;

    public VerifiedOrderTableEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
