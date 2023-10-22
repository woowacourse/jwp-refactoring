package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(final int numberOfGuests, final boolean empty) {
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
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
