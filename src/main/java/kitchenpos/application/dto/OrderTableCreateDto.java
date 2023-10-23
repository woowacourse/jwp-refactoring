package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableCreateDto {

    private final int numberOfGuests;

    @JsonCreator
    public OrderTableCreateDto(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
