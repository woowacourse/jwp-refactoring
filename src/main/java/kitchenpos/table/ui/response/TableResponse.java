package kitchenpos.table.ui.response;

import kitchenpos.table.domain.OrderTable;

public class TableResponse {
    private Long id;
    private GroupOfTableResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public TableResponse(Long id, GroupOfTableResponse tableGroupResponse, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroupResponse;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                GroupOfTableResponse.from(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public GroupOfTableResponse getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
