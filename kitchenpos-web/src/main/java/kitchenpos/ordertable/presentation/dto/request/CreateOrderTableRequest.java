package kitchenpos.ordertable.presentation.dto.request;

public class CreateOrderTableRequest {

    private int numberOfGuests;

    private boolean empty;

    private CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(final int numberOfGuests,
                                   final boolean empty) {
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
