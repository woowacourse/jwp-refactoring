package kitchenpos.ui.dto;

public class TableUpdateGuestNumberRequest {

    private final Integer numberOfGuests;

    public TableUpdateGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
