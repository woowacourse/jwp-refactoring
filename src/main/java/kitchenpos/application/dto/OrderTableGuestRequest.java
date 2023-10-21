package kitchenpos.application.dto;

public class OrderTableGuestRequest {

    private final int numberOfGuests;

    public OrderTableGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
