package kitchenpos.dto;

public class TableChangeEmptyDto {

    private final boolean empty;

    public TableChangeEmptyDto(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
