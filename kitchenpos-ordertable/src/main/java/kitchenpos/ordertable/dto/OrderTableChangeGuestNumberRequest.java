package kitchenpos.ordertable.dto;

public class OrderTableChangeGuestNumberRequest {

    private int numberOfGuests;

    public OrderTableChangeGuestNumberRequest() {
    }

    public OrderTableChangeGuestNumberRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
