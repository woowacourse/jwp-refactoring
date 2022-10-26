package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableEmptyChangeRequest {

    private boolean empty;

    private OrderTableEmptyChangeRequest() {
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, 0, empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
