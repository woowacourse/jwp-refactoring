package kitchenpos.ui.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
