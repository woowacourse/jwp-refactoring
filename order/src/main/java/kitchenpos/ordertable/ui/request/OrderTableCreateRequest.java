package kitchenpos.ordertable.ui.request;

import javax.validation.constraints.NotNull;

public class OrderTableCreateRequest {

    @NotNull
    private final Integer numberOfGuests;

    @NotNull
    private final Boolean empty;

    public OrderTableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
