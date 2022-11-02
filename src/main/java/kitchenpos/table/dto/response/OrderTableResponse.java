package kitchenpos.table.dto.response;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        id = orderTable.getId();
        tableGroupId = orderTable.getTableGroupId();
        numberOfGuests = orderTable.getNumberOfGuests();
        empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getEmpty() {
        return empty;
    }

}
