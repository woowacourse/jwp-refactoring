package kitchenpos.dto;

public class TableNumberOfGuestsUpdateRequest {

    private int numberOfGuests;

    public TableNumberOfGuestsUpdateRequest() {
    }

    public TableNumberOfGuestsUpdateRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
