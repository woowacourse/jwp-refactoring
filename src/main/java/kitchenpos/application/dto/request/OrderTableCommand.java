package kitchenpos.application.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableCommand {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableCommand(int numberOfGuests, boolean empty) {
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
        return new OrderTable(numberOfGuests, empty);
    }
}
