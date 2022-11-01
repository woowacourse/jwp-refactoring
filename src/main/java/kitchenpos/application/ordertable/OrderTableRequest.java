package kitchenpos.application.ordertable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    OrderTableRequest(int numberOfGuests, boolean empty) {
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
