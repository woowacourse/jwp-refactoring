package kitchenpos.dto.orderTable;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
