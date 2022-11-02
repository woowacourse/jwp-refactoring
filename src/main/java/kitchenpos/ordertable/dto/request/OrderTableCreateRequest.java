package kitchenpos.ordertable.dto.request;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
