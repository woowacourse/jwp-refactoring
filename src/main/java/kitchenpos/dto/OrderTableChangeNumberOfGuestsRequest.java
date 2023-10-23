package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    @JsonCreator
    public OrderTableChangeNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
