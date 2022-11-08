package kitchenpos.table.application.dto.request;

public class OrderTableUpdateNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest(final int numberOfGuests) {
        validateNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }
}
