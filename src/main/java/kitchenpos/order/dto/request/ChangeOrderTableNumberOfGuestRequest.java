package kitchenpos.order.dto.request;

public class ChangeOrderTableNumberOfGuestRequest {

    private int numberOfGuests;

    private ChangeOrderTableNumberOfGuestRequest() {
    }

    public ChangeOrderTableNumberOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
