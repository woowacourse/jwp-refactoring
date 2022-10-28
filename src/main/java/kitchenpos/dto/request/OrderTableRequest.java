package kitchenpos.dto.request;

public class OrderTableRequest {

    private int numberOfGuests;
    private Boolean empty;

    private OrderTableRequest() {};

    public OrderTableRequest(final int numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = true;
    }

    public OrderTableRequest(final Boolean empty) {
        this.numberOfGuests = 0;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
