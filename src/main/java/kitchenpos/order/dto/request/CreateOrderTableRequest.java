package kitchenpos.order.dto.request;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    private CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(int numberOfGuests, boolean empty) {
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
