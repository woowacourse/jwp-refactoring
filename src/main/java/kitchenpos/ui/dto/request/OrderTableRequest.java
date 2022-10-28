package kitchenpos.ui.dto.request;

import kitchenpos.application.dto.request.OrderTableCommand;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    private OrderTableRequest() {
    }

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTableCommand toCommand() {
        return new OrderTableCommand(numberOfGuests, empty);
    }
}
