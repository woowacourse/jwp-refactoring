package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableChangeNumberOfGuestsDto {

    private final int numberOfGuests;

    @JsonCreator
    public TableChangeNumberOfGuestsDto(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
