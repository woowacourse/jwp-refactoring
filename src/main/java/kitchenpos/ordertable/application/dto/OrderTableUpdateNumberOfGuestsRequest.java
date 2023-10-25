package kitchenpos.ordertable.application.dto;

import javax.validation.constraints.NotNull;

public class OrderTableUpdateNumberOfGuestsRequest {

    @NotNull
    private Integer numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest() {
    }

    public OrderTableUpdateNumberOfGuestsRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

}
