package kitchenpos.table.application;

public class TableGroupCreateEvent {

    private final Object source;
    private final Long orderTableId;

    public TableGroupCreateEvent(Object source,
                                 Long orderTableId) {
        this.source = source;
        this.orderTableId = orderTableId;
    }

    public Object getSource() {
        return source;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
