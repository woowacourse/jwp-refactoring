package kitchenpos.ui.dto.ordertable;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    OrderTableChangeNumberOfGuestsRequest() {

    }

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
