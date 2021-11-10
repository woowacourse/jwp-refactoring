package kitchenpos.order.ui.request;

import kitchenpos.order.domain.OrderTable;

public class OrderTableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public static OrderTableCreateRequest create(int numberOfGuests, boolean empty) {
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest();
        orderTableCreateRequest.numberOfGuests = numberOfGuests;
        orderTableCreateRequest.empty = empty;
        return orderTableCreateRequest;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return OrderTable.create(numberOfGuests, empty);
    }
}
