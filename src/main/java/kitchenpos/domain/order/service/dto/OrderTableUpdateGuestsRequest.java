package kitchenpos.domain.order.service.dto;

public class OrderTableUpdateGuestsRequest {

    private final int numberOfGuests;

    public OrderTableUpdateGuestsRequest(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
