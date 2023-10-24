package kitchenpos.ordertable.dto;

public class OrderTableUpdateRequest {

    private boolean empty;
    private int numberOfGuests;

    private OrderTableUpdateRequest() {
    }

    public OrderTableUpdateRequest(boolean empty, int numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
