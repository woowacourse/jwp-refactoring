package kitchenpos.ordertable.application.dto;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
