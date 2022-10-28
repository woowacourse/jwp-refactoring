package kitchenpos.ui.dto;

public class TableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
