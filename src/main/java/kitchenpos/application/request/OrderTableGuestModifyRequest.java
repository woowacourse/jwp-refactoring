package kitchenpos.application.request;

public class OrderTableGuestModifyRequest {

    private int numberOfGuests;

    public OrderTableGuestModifyRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
