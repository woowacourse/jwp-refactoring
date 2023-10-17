package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private final Long id;
    private final boolean empty;
    private final int numberOfGuests;
    private final Long tableGroupId;

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(),
            orderTable.isEmpty(),
            orderTable.getNumberOfGuests(),
            orderTable.getTableGroupId()
        );
    }

    public OrderTableResponse(Long id, boolean empty, int numberOfGuests, Long tableGroupId) {
        this.id = id;
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
        this.tableGroupId = tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
