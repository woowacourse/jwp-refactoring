package kitchenpos.ordertable.request;

public class OrderTableCreateRequest {

    private final int numberOfGuest;
    private final boolean empty;

    public OrderTableCreateRequest(int numberOfGuest, boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    public boolean isEmpty() {
        return empty;
    }
}
