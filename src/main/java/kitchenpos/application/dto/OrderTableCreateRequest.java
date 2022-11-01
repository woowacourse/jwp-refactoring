package kitchenpos.application.dto;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    protected OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(final int numberOfGuests, final boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}