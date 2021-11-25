package kitchenpos.dto;

public class OrderTableChangeGuestRequest {
    private int numberOfGuests;

    public OrderTableChangeGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
