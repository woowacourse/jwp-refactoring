package kitchenpos.ordertable.application.dto;

import javax.validation.constraints.Min;

public class OrderTableChangeGuestRequest {

    @Min(0)
    private int numberOfGuests;

    private OrderTableChangeGuestRequest() {
    }

    public OrderTableChangeGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
