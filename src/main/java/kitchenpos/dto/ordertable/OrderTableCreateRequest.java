package kitchenpos.dto.ordertable;

import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.table.OrderTable;

public class OrderTableCreateRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        NumberOfGuests numberOfGuests = NumberOfGuests.from(this.numberOfGuests);

        return OrderTable.of(numberOfGuests, this.empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
