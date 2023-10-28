package kitchenpos.ordertable.request;

import javax.validation.constraints.NotNull;

public class TableChangeNumberOfGuestsRequest {

    @NotNull
    private int numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
