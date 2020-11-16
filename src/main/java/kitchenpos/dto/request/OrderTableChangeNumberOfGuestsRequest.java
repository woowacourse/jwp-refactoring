package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeNumberOfGuestsRequest {
    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
