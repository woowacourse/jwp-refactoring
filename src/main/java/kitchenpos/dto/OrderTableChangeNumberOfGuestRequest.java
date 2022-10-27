package kitchenpos.dto;

public class OrderTableChangeNumberOfGuestRequest {

    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
