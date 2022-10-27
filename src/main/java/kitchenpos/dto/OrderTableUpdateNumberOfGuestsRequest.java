package kitchenpos.dto;

public class OrderTableUpdateNumberOfGuestsRequest {

    private int numberOfGuests;

    private OrderTableUpdateNumberOfGuestsRequest() {
    }

    public OrderTableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
