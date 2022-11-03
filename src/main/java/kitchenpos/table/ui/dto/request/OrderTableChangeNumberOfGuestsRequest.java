package kitchenpos.table.ui.dto.request;

import javax.validation.constraints.Min;

public class OrderTableChangeNumberOfGuestsRequest {

    @Min(0)
    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
