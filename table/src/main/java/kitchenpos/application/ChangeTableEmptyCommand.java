package kitchenpos.application;

public class ChangeTableEmptyCommand {
    private Long tableId;
    private boolean empty;

    public ChangeTableEmptyCommand() {
    }

    public ChangeTableEmptyCommand(final Long tableId, final boolean empty) {
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
