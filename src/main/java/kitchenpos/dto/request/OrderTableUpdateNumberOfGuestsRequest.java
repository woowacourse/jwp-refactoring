package kitchenpos.dto.request;

public class OrderTableUpdateNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
