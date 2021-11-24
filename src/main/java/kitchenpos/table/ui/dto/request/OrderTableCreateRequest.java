package kitchenpos.table.ui.dto.request;

import kitchenpos.table.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return OrderTable.create(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
