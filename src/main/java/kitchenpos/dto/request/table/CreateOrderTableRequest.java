package kitchenpos.dto.request.table;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

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
