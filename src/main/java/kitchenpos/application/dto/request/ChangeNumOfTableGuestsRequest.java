package kitchenpos.application.dto.request;

public class ChangeNumOfTableGuestsRequest {

    private int numberOfGuests;

    public ChangeNumOfTableGuestsRequest() {
    }

    public ChangeNumOfTableGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
