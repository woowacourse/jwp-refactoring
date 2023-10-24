package kitchenpos.table.application;

public class TableChangeEmptyEvent {

    private final Long orderTableId;

    public TableChangeEmptyEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
