package kitchenpos.common.dto.request;

public class PutOrderTableGuestsNumberRequest {

    private int numberOfGuests;

    public PutOrderTableGuestsNumberRequest() {
    }

    public PutOrderTableGuestsNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
