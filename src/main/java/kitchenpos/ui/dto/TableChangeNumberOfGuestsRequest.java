package kitchenpos.ui.dto;

public class TableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public String toString() {
        return "TableChangeNumberOfGuestsRequest{" +
            "numberOfGuests=" + numberOfGuests +
            '}';
    }
}
