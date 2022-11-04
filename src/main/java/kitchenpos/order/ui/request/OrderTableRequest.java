package kitchenpos.order.ui.request;

import kitchenpos.table.application.request.OrderTableCommand;

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
