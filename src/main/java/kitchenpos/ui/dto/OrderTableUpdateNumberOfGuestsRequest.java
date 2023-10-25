package kitchenpos.ui.dto;

public class OrderTableUpdateNumberOfGuestsRequest {

    private int numberOfGuests;

    public OrderTableUpdateNumberOfGuestsRequest() {
    }

    public OrderTableUpdateNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
