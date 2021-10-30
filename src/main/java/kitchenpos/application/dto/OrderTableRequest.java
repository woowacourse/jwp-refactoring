package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public OrderTableRequest() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
