package kitchenpos.table.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public TableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
