package kitchenpos.application.table;

public class ChangeEmptyEvent {

    private final Long orderTableId;

    public ChangeEmptyEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
