package kitchenpos.dto.request.table;

public class ChangeTableGuestRequest {
    private int numberOfGuests;

    public ChangeTableGuestRequest() {
    }

    public ChangeTableGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
