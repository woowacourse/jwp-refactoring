package kitchenpos.ordertable.application.dto;

public class OrderTableCreateRequest {
    private int numberOfGuests;
    private boolean empty;

    private OrderTableCreateRequest() {
        this.numberOfGuests = 0;
        this.empty = true;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
