package kitchenpos.table.application;

public class TableGroupCreateEvent {

    private final Long orderTableId;

    public TableGroupCreateEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
