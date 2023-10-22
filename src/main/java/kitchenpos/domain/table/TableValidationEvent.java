package kitchenpos.domain.table;

public class TableValidationEvent {

    private final Long tableId;

    public TableValidationEvent(final Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }
}
