package kitchenpos.vo.ordertable;

public class OrderTableRequest {

    private final int numberOfGuest;
    private final boolean empty;

    public OrderTableRequest(int numberOfGuest, boolean empty) {
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
