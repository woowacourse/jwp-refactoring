package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableChangeEmptyResponse {
    private boolean empty;

    private TableChangeEmptyResponse() {
    }

    public TableChangeEmptyResponse(final boolean empty) {
        this.empty = empty;
    }

    public static TableChangeEmptyResponse from(final OrderTable orderTable) {
        return new TableChangeEmptyResponse(orderTable.isEmpty());
    }

    public boolean isEmpty() {
        return empty;
    }
}
