package kitchenpos.table.application.dto.request;

import javax.validation.constraints.PositiveOrZero;

public class OrderTableChangeNumberOfGuestRequest {

    @PositiveOrZero
    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestRequest() {
    }

    public OrderTableChangeNumberOfGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
