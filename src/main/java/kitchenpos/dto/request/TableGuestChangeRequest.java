package kitchenpos.dto.request;

public class TableGuestChangeRequest {

    private int numberOfGuests;

    public TableGuestChangeRequest() {
    }

    public TableGuestChangeRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
