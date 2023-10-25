package kitchenpos.ordertable.dto;

public class OrderTableUpdateRequest {

    private int numberOfGuests;
    private boolean empty;

    protected OrderTableUpdateRequest() {
    }

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
