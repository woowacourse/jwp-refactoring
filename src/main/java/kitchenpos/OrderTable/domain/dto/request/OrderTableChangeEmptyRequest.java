package kitchenpos.OrderTable.domain.dto.request;

import kitchenpos.OrderTable.domain.OrderTable;

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
