package kitchenpos.request;

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
}
