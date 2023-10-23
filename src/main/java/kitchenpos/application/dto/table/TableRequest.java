package kitchenpos.application.dto.table;

public class TableRequest {

    private int numberOfGuests;
    private boolean empty;

    private TableRequest() {}

    public TableRequest(final int numberOfGuests, final boolean empty) {
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
