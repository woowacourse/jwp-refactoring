package kitchenpos.dto.ordertable;

public class ChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public ChangeNumberOfGuestsRequest(int numberOfGuest) {
        this.numberOfGuests = numberOfGuest;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
