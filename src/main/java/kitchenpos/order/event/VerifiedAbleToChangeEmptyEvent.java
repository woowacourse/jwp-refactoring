package kitchenpos.order.event;

public class VerifiedAbleToChangeEmptyEvent {

    private Long orderTableId;

    public VerifiedAbleToChangeEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

}
