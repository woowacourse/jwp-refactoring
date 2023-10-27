package kitchenpos.order.request;

public class TableUpdateRequest {

    private int numberOfGuests;
    private boolean empty;

    public TableUpdateRequest(int numberOfGuests, boolean empty) {
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
