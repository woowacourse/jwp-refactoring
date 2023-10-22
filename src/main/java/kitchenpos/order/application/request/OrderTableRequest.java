package kitchenpos.order.application.request;

public class OrderTableRequest {
    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
