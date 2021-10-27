package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

import javax.validation.constraints.NotNull;

public class OrderTableRequest {
    @NotNull
    private int numberOfGuests;
    @NotNull
    private boolean empty;

    public OrderTableRequest() {}

    private OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
