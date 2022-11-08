package kitchenpos.order.dto;

public class TableGuestChangeRequest {
    private final int numberOfGuests;

    public TableGuestChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
