package kitchenpos.ui.dto;

public class CreateOrderTableRequest {

    private int numberOfGuests;

    private boolean empty;

    public CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }
}
