package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeNumberOfGuestsRequest(@JsonProperty("numberOfGuests") final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
