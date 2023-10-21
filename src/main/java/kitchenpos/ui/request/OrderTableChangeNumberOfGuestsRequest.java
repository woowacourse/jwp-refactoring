package kitchenpos.ui.request;

import javax.validation.constraints.NotNull;

public class OrderTableChangeNumberOfGuestsRequest {

    @NotNull
    private final Integer numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
