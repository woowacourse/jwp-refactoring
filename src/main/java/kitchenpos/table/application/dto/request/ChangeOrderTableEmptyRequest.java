package kitchenpos.table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeOrderTableEmptyRequest {

    private final boolean empty;

    @JsonCreator
    public ChangeOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
