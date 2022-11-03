package kitchenpos.ui.jpa.dto.ordertable;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
