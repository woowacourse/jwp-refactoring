package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableNumOfGuestRequest {
    private int numberOfGuests;

    @JsonCreator
    public OrderTableNumOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
