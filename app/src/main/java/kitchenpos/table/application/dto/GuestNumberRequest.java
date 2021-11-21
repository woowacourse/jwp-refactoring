package kitchenpos.table.application.dto;

import javax.validation.constraints.Size;

public class GuestNumberRequest {
    @Size
    private int numberOfGuests;

    public GuestNumberRequest() {
    }

    public GuestNumberRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
