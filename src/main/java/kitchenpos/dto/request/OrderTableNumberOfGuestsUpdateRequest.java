package kitchenpos.dto.request;

public class OrderTableNumberOfGuestsUpdateRequest {

    private int numberOfGuests;

    private OrderTableNumberOfGuestsUpdateRequest() {}

    public OrderTableNumberOfGuestsUpdateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
