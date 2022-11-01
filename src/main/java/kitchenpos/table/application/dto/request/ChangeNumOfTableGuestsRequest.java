package kitchenpos.table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeNumOfTableGuestsRequest {

    private final int numberOfGuests;

    @JsonCreator
    public ChangeNumOfTableGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
