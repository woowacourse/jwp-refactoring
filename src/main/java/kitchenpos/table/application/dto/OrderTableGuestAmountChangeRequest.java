package kitchenpos.table.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableGuestAmountChangeRequest {

    private final Integer numberOfGuests;

    @JsonCreator
    public OrderTableGuestAmountChangeRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
