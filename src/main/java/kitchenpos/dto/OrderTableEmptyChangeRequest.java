package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableEmptyChangeRequest {

    private boolean empty;

    private OrderTableEmptyChangeRequest() {
    }

    public OrderTable toOrderTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public boolean isEmpty() {
        return empty;
    }
}
