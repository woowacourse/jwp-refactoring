package kitchenpos.dto.table;

import javax.validation.constraints.Min;

public class OrderTableChangeNumberOfGuestsRequest {

    @Min(value = 0)
    private int numberOfGuests;

    protected OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
