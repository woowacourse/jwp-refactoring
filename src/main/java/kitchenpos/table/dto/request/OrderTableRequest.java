package kitchenpos.table.dto.request;

import kitchenpos.table.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
