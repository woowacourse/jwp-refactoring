package kitchenpos.table.dto;

import kitchenpos.table.OrderTable;

public class OrderTableSaveRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableSaveRequest() {
    }

    public OrderTableSaveRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
