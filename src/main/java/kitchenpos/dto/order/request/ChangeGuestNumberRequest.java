package kitchenpos.dto.order.request;

public class ChangeGuestNumberRequest {

    private int numberOfGuests;

    private ChangeGuestNumberRequest() {
    }

    public ChangeGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
