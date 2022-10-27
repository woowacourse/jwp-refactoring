package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
