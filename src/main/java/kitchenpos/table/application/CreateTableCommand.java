package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

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
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

}
