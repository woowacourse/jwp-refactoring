package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableCreationRequest {

    private final Integer numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public OrderTableCreationRequest(final Integer numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
