package kitchenpos.domain.order;

public class OrderTableExistValidationEvent {

    private final Long tableId;

    public OrderTableExistValidationEvent(final Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }
}
