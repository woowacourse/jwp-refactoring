package table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeEmptyRequest {

    private final boolean empty;

    @JsonCreator
    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
