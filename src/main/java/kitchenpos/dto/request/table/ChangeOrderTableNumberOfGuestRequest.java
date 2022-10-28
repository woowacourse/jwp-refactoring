package kitchenpos.dto.request.table;

public class ChangeOrderTableNumberOfGuestRequest {

    private int numberOfGuests;

    public ChangeOrderTableNumberOfGuestRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
