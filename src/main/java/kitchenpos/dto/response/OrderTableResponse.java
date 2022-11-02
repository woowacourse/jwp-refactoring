package kitchenpos.dto.response;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(final Long id, final Long tableGroupId,
                              final Integer numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
