package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class ChangeNumberOfGuestsRequest {

    @NotNull
    int numberOfGuests;

    public ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
