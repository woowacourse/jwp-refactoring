package kitchenpos.dto.table;

public class TableUpdateNumberOfGuestsRequest {

    private final int numberOfGuests;

    private TableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static TableUpdateNumberOfGuestsRequest from(final int numberOfGuests) {
        return new TableUpdateNumberOfGuestsRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
