package kitchenpos.dto.ordertable;

public class OrderTableChangNumberOfGuestRequest {

    private int numberOfGuests;

    public OrderTableChangNumberOfGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
