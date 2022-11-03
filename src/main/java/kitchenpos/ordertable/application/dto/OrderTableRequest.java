package kitchenpos.ordertable.application.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
