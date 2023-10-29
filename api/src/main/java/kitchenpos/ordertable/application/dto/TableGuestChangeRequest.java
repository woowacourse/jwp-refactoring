package kitchenpos.ordertable.application.dto;

public class TableGuestChangeRequest {

    private int numberOfGuests;

    private TableGuestChangeRequest() {
    }

    public TableGuestChangeRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
