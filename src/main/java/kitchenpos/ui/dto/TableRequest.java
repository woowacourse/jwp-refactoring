package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

import javax.validation.constraints.NotNull;

public class TableRequest {
    @NotNull
    private int numberOfGuests;
    @NotNull
    private boolean empty;

    private TableRequest() {
    }

    private TableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableRequest of(int numberOfGuests, boolean empty) {
        return new TableRequest(numberOfGuests, empty);
    }

    public static TableRequest empty(boolean empty) {
        return new TableRequest(0, empty);
    }

    public static TableRequest guests(int numberOfGuests) {
        return new TableRequest(numberOfGuests, false);
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
