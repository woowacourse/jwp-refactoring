package table.dto;

import table.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean orderable;

    private OrderTableResponse(Long id, int numberOfGuests, boolean orderable, Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.orderable = orderable;
        this.tableGroupId = tableGroupId;
    }

    public static OrderTableResponse of(final OrderTable orderTable) {
        if (orderTable.getTableGroupId().isEmpty()) {
            return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isOrderable(), null);
        }
        final Long tableGroupId = orderTable.getTableGroupId().get();
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isOrderable(), tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
