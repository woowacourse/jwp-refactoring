package kitchenpos.ui.dto;

public class OrderTableUpdateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableUpdateRequest(final int numberOfGuests, final boolean empty) {
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
