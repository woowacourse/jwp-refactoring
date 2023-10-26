package kitchenpos.order.request;

public class TableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public TableCreateRequest(int numberOfGuests, boolean empty) {
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
