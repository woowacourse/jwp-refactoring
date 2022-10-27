package kitchenpos.dto.request;

public class NumberOfGuestsRequest {

    private int numberOfGuests;

    private NumberOfGuestsRequest() {
    }

    public NumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
