package kitchenpos.order.presentation.dto.response;

import kitchenpos.order.domain.OrderTable;

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
