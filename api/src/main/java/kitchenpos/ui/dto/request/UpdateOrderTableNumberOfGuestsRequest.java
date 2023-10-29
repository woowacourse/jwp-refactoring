package kitchenpos.ui.dto.request;

public class UpdateOrderTableNumberOfGuestsRequest {

    private int numberOfGuests;

    public UpdateOrderTableNumberOfGuestsRequest() {
    }

    public UpdateOrderTableNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
