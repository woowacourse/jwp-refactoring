package kitchenpos.ordertable.domain.dto.request;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    protected OrderTableChangeEmptyRequest() {
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(empty);
    }
}
