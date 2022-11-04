package kitchenpos.table.dto;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    private OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
