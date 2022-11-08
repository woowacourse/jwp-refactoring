package kitchenpos.table.dto.request;

public class OrderTableUpdateGuestRequest {

    private int numberOfGuests;

    public OrderTableUpdateGuestRequest() {
    }

    public OrderTableUpdateGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
