package kitchenpos.order.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderTableChangeGuestRequest {

    private Integer numberOfGuests;

    @JsonCreator
    public OrderTableChangeGuestRequest(@JsonProperty("numberOfGuests") final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
