package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class ChangeOrderTableEmptyRequest {

    private boolean empty;

    public ChangeOrderTableEmptyRequest() {
    }

    public ChangeOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
