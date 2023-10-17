package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class CreateTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public CreateTableRequest() {
    }

    public CreateTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        OrderTable orderTable = new OrderTable();

        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        return orderTable;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
