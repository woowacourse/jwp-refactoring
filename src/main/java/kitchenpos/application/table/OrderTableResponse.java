package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public static OrderTableResponse from(final OrderTable orderTable) {
        var tableGroup = orderTable.getTableGroup();
        return new OrderTableResponse(
                orderTable.getId(),
                tableGroup == null ? null : tableGroup.getId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }
}
