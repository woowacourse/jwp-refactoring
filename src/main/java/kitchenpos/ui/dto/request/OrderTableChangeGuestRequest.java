package kitchenpos.ui.dto.request;

public class OrderTableChangeGuestRequest {

    private int numberOfGuests;

    public OrderTableChangeGuestRequest() {
    }

    public OrderTableChangeGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
