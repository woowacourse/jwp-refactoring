package kitchenpos.application.ordertable;

public class OrderTableChangeGuestsRequest {

    private int numberOfGuests;

    private OrderTableChangeGuestsRequest() {
    }

    OrderTableChangeGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
