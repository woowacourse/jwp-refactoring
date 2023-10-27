package kitchenpos.request;

public class TableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public TableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
