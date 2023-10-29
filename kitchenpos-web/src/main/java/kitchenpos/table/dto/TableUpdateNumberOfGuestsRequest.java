package kitchenpos.table.dto;

public class TableUpdateNumberOfGuestsRequest {

    private int numberOfGuests;

    private TableUpdateNumberOfGuestsRequest() {
    }

    public TableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
