package kitchenpos.ui;

public class PutNumberOfGuestsRequest {

    private int numberOfGuests;

    public PutNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

}
