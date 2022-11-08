package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class OrderTableChangeGuestNumberRequest {

    @NotNull
    private Integer numberOfGuests;

    protected OrderTableChangeGuestNumberRequest() {
    }

    public OrderTableChangeGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
