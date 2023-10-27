package kitchenpos.table.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeGuestRequest {

    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
