package kitchenpos.order.application.dto;

public class OrderTableGuestRequest {

    private int numberOfGuests;

    private OrderTableGuestRequest() {
    }

    public OrderTableGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
