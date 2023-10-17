package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class CreateTableCommand {

    private int numberOfGuests;
    private boolean empty;

    public CreateTableCommand() {
    }

    public CreateTableCommand(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        OrderTable orderTable = new OrderTable();

        orderTable.changeNumberOfGuests(numberOfGuests);
        orderTable.changeEmpty(empty);
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
