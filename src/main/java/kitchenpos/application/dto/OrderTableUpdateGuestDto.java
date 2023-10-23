package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableUpdateGuestDto {

    private final Integer numberOfGuests;

    @JsonCreator
    public OrderTableUpdateGuestDto(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
