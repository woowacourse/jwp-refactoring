package kitchenpos.table.event;

public class ChangedEmptyEvent {

    private final Long orderTableId;

    public ChangedEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
