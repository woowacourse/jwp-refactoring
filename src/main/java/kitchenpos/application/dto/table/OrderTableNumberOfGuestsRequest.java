package kitchenpos.application.dto.table;

public class OrderTableNumberOfGuestsRequest {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private int numberOfGuests;

    public OrderTableNumberOfGuestsRequest() {
    }

    public OrderTableNumberOfGuestsRequest(final int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("방문자 수는 0명 이상이여야 합니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
