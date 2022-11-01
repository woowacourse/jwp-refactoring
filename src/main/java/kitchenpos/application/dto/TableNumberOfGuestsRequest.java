package kitchenpos.application.dto;

public class TableNumberOfGuestsRequest {

    private int numberOfGuests;

    public TableNumberOfGuestsRequest() {
    }

    public TableNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
