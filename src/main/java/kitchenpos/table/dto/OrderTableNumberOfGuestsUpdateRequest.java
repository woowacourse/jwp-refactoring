package kitchenpos.table.dto;

public class OrderTableNumberOfGuestsUpdateRequest {

    private int numberOfGuests;

    public OrderTableNumberOfGuestsUpdateRequest() {
    }

    public OrderTableNumberOfGuestsUpdateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
