package kitchenpos.dto;

public class NumberOfGuestsChangeRequest {
    private int numberOfGuests;

    private NumberOfGuestsChangeRequest() {
    }

    public NumberOfGuestsChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
