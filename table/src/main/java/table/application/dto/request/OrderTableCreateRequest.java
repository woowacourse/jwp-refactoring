package table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
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
