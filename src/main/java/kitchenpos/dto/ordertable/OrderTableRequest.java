package kitchenpos.dto.ordertable;

public class OrderTableRequest {

    private Integer numberOfGuests;
    private boolean empty;

    OrderTableRequest() {
    }

    public OrderTableRequest(final Integer numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
