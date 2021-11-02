package kitchenpos.ui.request;

public class OrderTableChangeGuestsRequest {

    private int numberOfGuests;

    public static OrderTableChangeGuestsRequest create(int numberOfGuests) {
        final OrderTableChangeGuestsRequest orderTableChangeGuestsRequest = new OrderTableChangeGuestsRequest();
        orderTableChangeGuestsRequest.numberOfGuests = numberOfGuests;
        return orderTableChangeGuestsRequest;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
