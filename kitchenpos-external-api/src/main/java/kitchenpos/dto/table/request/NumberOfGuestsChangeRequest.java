package kitchenpos.dto.table.request;

public class NumberOfGuestsChangeRequest {

    private int numberOfGuests;

    private NumberOfGuestsChangeRequest() {
    }

    public NumberOfGuestsChangeRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
