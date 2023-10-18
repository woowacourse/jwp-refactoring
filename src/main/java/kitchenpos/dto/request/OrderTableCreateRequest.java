package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean empty() {
        return empty;
    }
}
