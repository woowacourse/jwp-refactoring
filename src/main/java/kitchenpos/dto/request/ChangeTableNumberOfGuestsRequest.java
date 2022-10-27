package kitchenpos.dto.request;

public class ChangeTableNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeTableNumberOfGuestsRequest() {
    }

    public ChangeTableNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
