package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final int numberOfGuests,
                                   final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, this.numberOfGuests, this.empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
