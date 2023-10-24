package kitchenpos.dto.request;

public class UpdateOrderTableGuestsRequest {
    private int numberOfGuests;

    public UpdateOrderTableGuestsRequest() {
    }

    public UpdateOrderTableGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
