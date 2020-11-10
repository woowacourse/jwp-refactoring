package kitchenpos.dto;

public class OrderTableChangeRequest {
    private int numberOfGuests;
    private boolean empty;

    public OrderTableChangeRequest() {
    }

    public OrderTableChangeRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableChangeRequest(boolean empty) {
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
