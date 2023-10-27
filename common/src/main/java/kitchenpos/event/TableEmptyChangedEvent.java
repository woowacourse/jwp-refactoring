package kitchenpos.event;

public class TableEmptyChangedEvent {

    private final Long orderTableId;

    public TableEmptyChangedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long orderTableId() {
        return orderTableId;
    }
}
