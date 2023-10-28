package kitchenpos.table.ui.dto;

public class ChangeOrderTableGuestRequest {
    private int numberOfGuests;

    public ChangeOrderTableGuestRequest() {
    }

    public ChangeOrderTableGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
