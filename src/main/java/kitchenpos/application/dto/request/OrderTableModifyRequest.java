package kitchenpos.application.dto.request;

public class OrderTableModifyRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableModifyRequest() {
    }

    public OrderTableModifyRequest(int numberOfGuests, boolean empty) {
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
