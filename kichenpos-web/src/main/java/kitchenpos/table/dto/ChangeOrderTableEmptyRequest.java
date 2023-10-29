package kitchenpos.table.dto;

public class ChangeOrderTableEmptyRequest {

    private boolean empty;

    public ChangeOrderTableEmptyRequest() {
    }

    public ChangeOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public ChangeOrderTableEmptyDto toChangeOrderTableEmptyDto() {
        return new ChangeOrderTableEmptyDto(empty);
    }
}
