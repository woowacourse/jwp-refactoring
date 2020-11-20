package kitchenpos.dto.request;

import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeNumberOfGuestsRequest {

    @PositiveOrZero
    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
