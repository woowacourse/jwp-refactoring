package kitchenpos.ordertable.application.dto;

public class ChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public ChangeNumberOfGuestsRequest(int numberOfGuest) {
        this.numberOfGuests = numberOfGuest;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
