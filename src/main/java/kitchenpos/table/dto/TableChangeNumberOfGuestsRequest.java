package kitchenpos.table.dto;

public class TableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
