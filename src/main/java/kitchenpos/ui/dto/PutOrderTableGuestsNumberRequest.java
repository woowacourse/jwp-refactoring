package kitchenpos.ui.dto;

public class PutOrderTableGuestsNumberRequest {

    private int numberOfGuests;

    public PutOrderTableGuestsNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
