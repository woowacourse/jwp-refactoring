package kitchenpos.dto.request;

public class CreateTableRequest {
    private int numberOfGuests;
    private boolean empty;

    public CreateTableRequest() {
    }

    public CreateTableRequest(int numberOfGuests, boolean empty) {
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
