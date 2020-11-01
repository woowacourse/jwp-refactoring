package kitchenpos.ui.dto;

public class TableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
