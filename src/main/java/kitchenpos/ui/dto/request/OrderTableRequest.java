package kitchenpos.ui.dto.request;

public class OrderTableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
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
