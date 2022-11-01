package kitchenpos.ui.dto;

public class TableUpdateGuestNumberRequest {

    private Integer numberOfGuests;

    public TableUpdateGuestNumberRequest() {
    }

    public TableUpdateGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
