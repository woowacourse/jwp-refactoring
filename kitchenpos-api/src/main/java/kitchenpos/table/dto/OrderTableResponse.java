package kitchenpos.table.dto;

import kitchenpos.table.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse() {
    }

    public OrderTableResponse(final OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = orderTable.getTableGroupId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
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

    public boolean isEmpty() {
        return empty;
    }
}
