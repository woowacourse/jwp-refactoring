package kitchenpos.application.dto.request;

public class TableUpdateNumberOfGuestsRequest {

    private final int numberOfGuests;

    public TableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
