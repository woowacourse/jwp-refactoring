package kitchenpos.ui.dto;

import kitchenpos.domain.Table;

public class TableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    private TableCreateRequest() {
    }

    public TableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Table toOrderTable() {
        return Table.entityOf(numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "TableCreateRequest{" +
            "numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
