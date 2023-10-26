package kitchenpos.table.dto.request;

public class OrderTableChangeNumberOfGuestsRequest {

    private final int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
