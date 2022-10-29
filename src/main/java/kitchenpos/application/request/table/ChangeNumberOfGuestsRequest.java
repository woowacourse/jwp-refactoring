package kitchenpos.application.request.table;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
