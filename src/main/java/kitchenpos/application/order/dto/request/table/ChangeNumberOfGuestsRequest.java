package kitchenpos.application.order.dto.request.table;

import kitchenpos.domain.order.GuestCount;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public GuestCount getNumberOfGuests() {
        return new GuestCount(numberOfGuests);
    }
}
