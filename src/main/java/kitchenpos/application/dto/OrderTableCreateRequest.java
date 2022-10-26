package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableCreateRequest() {
        this(0, false);
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
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
