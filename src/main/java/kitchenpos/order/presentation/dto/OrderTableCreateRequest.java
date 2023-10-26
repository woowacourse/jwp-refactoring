package kitchenpos.order.presentation.dto;

public class OrderTableCreateRequest {

    private int numberOfGuest;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

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
