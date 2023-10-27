package kitchenpos.dto.ordertable.request;

public class OrderTableChangeNumberOfGuestsRequest {
    final int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }
}
