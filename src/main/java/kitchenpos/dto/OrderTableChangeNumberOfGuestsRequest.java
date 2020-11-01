package kitchenpos.dto;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    protected OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
