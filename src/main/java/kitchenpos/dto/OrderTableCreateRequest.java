package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {
    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
        this.numberOfGuests = 0;
        this.empty = true;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
