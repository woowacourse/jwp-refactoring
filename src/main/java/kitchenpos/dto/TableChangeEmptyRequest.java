package kitchenpos.dto;

import java.beans.ConstructorProperties;

public class TableChangeEmptyRequest {
    private final boolean empty;

    @ConstructorProperties({"empty"})
    public TableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
