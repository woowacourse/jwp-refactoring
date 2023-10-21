package kitchenpos.dto.table;

public class TableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    private TableCreateRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableCreateRequest of(final int numberOfGuests, final boolean empty) {
        return new TableCreateRequest(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
