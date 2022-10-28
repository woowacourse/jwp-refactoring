package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class TableEmptyUpdateRequest {

    private boolean empty;

    private TableEmptyUpdateRequest() {
    }

    public TableEmptyUpdateRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public OrderTable toUpdateEntity(OrderTable orderTable) {
        return new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                isEmpty());
    }
}
