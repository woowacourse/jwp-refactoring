package table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
