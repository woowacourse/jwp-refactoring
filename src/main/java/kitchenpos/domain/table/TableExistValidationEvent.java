package kitchenpos.domain.table;

public class TableExistValidationEvent {

    private final Long tableId;

    public TableExistValidationEvent(final Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }
}
