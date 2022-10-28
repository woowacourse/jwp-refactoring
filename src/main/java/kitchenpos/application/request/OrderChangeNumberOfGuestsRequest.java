package kitchenpos.application.request;

public class OrderChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    private OrderChangeNumberOfGuestsRequest() {
    }

    public OrderChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
