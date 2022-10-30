package kitchenpos.application.dto;

public class ChangeNumberOfGuestRequest {

    private int numberOfGuests;

    private ChangeNumberOfGuestRequest() {
    }

    public ChangeNumberOfGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
