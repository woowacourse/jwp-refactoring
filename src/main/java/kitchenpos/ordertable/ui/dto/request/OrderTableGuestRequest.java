package kitchenpos.ordertable.ui.dto.request;

public class OrderTableGuestRequest {

    private int numberOfGuests;

    public OrderTableGuestRequest() {
    }

    public OrderTableGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
