package kitchenpos.dto.request;

public class TableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public TableCreateRequest(final int numberOfGuests, final boolean empty) {
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
