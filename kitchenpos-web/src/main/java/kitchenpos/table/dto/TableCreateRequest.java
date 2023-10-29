package kitchenpos.table.dto;

public class TableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    private TableCreateRequest() {
    }

    public TableCreateRequest(
            final int numberOfGuests,
            final boolean empty
    ) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
