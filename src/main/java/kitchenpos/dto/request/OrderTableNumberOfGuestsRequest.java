package kitchenpos.dto.request;

public class OrderTableNumberOfGuestsRequest {

    private int numberOfGuests;

    protected OrderTableNumberOfGuestsRequest() {
    }

    public OrderTableNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
