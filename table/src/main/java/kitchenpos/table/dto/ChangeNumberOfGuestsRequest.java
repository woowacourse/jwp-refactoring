package kitchenpos.table.dto;

public class ChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    private ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ChangeNumberOfGuestsRequest() {
    }

    public static ChangeNumberOfGuestsRequest of(final int numberOfGuests) {
        return new ChangeNumberOfGuestsRequest(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
