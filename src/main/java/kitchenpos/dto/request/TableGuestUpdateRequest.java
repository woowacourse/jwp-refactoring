package kitchenpos.dto.request;

import javax.validation.constraints.Min;

public class TableGuestUpdateRequest {

    @Min(0)
    private int numberOfGuests;

    protected TableGuestUpdateRequest() {
    }

    public TableGuestUpdateRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
