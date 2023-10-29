package kitchenpos.table.dto;

public class ChangeOrderTableEmptyDto {

    private final boolean empty;

    public ChangeOrderTableEmptyDto(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
