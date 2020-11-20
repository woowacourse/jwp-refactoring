package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableCreateRequest {
    private int numberOfGuests;
    private boolean empty;

    private TableCreateRequest() {
    }

    public TableCreateRequest(int numberOfGuests, boolean empty) {
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
