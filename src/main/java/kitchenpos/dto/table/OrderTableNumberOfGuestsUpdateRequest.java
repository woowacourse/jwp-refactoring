package kitchenpos.dto.table;

public class OrderTableNumberOfGuestsUpdateRequest {

    private final int numberOfGuests;

    public OrderTableNumberOfGuestsUpdateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
