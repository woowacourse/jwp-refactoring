package kitchenpos.dto.ordertable;

import kitchenpos.domain.table.NumberOfGuests;

public class OrderTableChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public NumberOfGuests toNumberOfGuests() {
        return NumberOfGuests.from(this.numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
