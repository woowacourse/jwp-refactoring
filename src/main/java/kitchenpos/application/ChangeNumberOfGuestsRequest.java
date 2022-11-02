package kitchenpos.application;

public class ChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    private ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
