package kitchenpos.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotNull;

public class TableChangeEmptyRequest {
    @NotNull
    private final Boolean empty;

    @ConstructorProperties({"empty"})
    public TableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
