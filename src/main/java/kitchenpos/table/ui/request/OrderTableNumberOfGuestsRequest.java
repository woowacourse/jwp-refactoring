package kitchenpos.table.ui.request;

public class OrderTableNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableNumberOfGuestsRequest() {
    }

    public OrderTableNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
