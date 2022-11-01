package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderTable;

public class TableChangeEmptyResponse {

    private boolean empty;

    private TableChangeEmptyResponse() {
    }

    public TableChangeEmptyResponse(OrderTable table) {
        this.empty = table.isEmpty();
    }

    public boolean isEmpty() {
        return empty;
    }
}
