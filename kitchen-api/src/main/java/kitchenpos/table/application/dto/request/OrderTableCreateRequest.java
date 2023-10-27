package kitchenpos.table.application.dto.request;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableCreateRequest() {
    }

    public OrderTable toOrderTable() {
        return new OrderTable(NumberOfGuests.from(numberOfGuests), empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
