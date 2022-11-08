package kitchenpos.table.dto;

public class ChangeGuestNumberRequest {

    private int numberOfGuests;

    public ChangeGuestNumberRequest() {
    }

    public ChangeGuestNumberRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
