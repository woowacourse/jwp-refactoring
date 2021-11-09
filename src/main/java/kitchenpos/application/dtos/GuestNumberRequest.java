package kitchenpos.application.dtos;

import javax.validation.constraints.Size;

public class GuestNumberRequest {
    @Size(min = 0)
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
