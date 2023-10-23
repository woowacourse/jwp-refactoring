package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class TableNumberOfGuestsUpdateRequest {

    @NotNull
    private final int numberOfGuests;

    @JsonCreator
    public TableNumberOfGuestsUpdateRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
