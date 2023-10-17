package kitchenpos.application.dto;

public class ChangeTableEmptyRequest {
    private Long tableId;
    private boolean empty;

    public ChangeTableEmptyRequest() {
    }

    public ChangeTableEmptyRequest(final Long tableId, final boolean empty) {
        this.tableId = tableId;
        this.empty = empty;
    }

    public Long getTableId() {
        return tableId;
    }

    public boolean getEmpty() {
        return empty;
    }

}
