package kitchenpos.application.dto;

public class OrderTableChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    private OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
