package kitchenpos.dto.request.table;

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
