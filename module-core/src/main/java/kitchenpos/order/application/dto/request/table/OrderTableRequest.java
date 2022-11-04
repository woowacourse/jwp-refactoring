package kitchenpos.order.application.dto.request.table;

import kitchenpos.order.domain.GuestCount;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public GuestCount getNumberOfGuests() {
        return new GuestCount(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }
}
