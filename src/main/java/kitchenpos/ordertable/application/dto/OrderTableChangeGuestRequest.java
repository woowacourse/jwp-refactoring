package kitchenpos.ordertable.application.dto;

import javax.validation.constraints.Min;

public class OrderTableChangeGuestRequest {

    @Min(0)
    private final int numberOfGuests;

    public OrderTableChangeGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
