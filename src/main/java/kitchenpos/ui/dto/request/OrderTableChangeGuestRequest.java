package kitchenpos.ui.dto.request;

public class OrderTableChangeGuestRequest {

    private int numberOfGuests;

    private OrderTableChangeGuestRequest() {
    }

    public OrderTableChangeGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
