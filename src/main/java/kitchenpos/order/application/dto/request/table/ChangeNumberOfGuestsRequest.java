package kitchenpos.order.application.dto.request.table;

import kitchenpos.order.domain.GuestCount;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public GuestCount getNumberOfGuests() {
        return new GuestCount(numberOfGuests);
    }
}
