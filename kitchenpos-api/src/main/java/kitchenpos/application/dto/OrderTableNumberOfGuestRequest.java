package kitchenpos.application.dto;

public class OrderTableNumberOfGuestRequest {
    private int numberOfGuests;

    public OrderTableNumberOfGuestRequest() {
    }

    public OrderTableNumberOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
