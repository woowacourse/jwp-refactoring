package kitchenpos.ordertable.dto;

public class OrderTableGuestsChangeRequest {
    private int numberOfGuests;

    public OrderTableGuestsChangeRequest() {
    }

    public OrderTableGuestsChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
