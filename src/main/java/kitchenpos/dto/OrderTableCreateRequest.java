package kitchenpos.dto;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
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
