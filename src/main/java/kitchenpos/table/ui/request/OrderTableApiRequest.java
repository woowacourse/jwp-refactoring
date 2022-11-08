package kitchenpos.table.ui.request;

import kitchenpos.table.application.request.OrderTableRequest;

public class OrderTableApiRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableApiRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableRequest toServiceRequest() {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
