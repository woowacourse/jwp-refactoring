package kitchenpos.ui.dto;

public class NumberOfGuestsRequest {
    private int numberOfGuests;

    public NumberOfGuestsRequest() {
    }

    public NumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
