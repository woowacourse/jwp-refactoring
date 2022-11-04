package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderTableRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableRequest(final int numberOfGuests) {
        this(numberOfGuests, null);
    }

    public OrderTableRequest(final boolean empty) {
        this(null, empty);
    }

    @JsonCreator
    public OrderTableRequest(final Integer numberOfGuests, final Boolean empty) {
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
