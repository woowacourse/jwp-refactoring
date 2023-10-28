package kitchenpos.dto.request;

public class OrderTableUpdateNumberOfGuestRequest {

    private int numberOfGuests;

    protected OrderTableUpdateNumberOfGuestRequest() {
    }

    public OrderTableUpdateNumberOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
