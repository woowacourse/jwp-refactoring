package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableChangeEmptyDto {

    private final boolean empty;

    @JsonCreator
    public TableChangeEmptyDto(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
