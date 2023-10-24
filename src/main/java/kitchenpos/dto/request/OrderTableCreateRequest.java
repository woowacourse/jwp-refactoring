package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
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
