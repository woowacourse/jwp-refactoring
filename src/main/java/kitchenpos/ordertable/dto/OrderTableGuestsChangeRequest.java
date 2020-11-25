package kitchenpos.ordertable.dto;

import javax.validation.constraints.NotNull;

public class OrderTableGuestsChangeRequest {
    @NotNull
    private Integer numberOfGuests;

    public OrderTableGuestsChangeRequest() {
    }

    public OrderTableGuestsChangeRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
