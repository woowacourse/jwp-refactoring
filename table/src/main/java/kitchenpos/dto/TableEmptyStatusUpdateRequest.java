package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableEmptyStatusUpdateRequest {

    private final boolean empty;

    @JsonCreator
    public TableEmptyStatusUpdateRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
