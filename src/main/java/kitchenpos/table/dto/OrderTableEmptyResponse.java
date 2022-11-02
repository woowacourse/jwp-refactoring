package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableEmptyResponse {

    private final boolean empty;

    private OrderTableEmptyResponse(boolean empty) {
        this.empty = empty;
    }

    public static OrderTableEmptyResponse from (OrderTable orderTable){
        return new OrderTableEmptyResponse(orderTable.isEmpty());
    }

    public boolean isEmpty() {
        return empty;
    }
}
