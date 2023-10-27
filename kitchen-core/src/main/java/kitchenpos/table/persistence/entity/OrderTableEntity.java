package kitchenpos.table.persistence.entity;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class OrderTableEntity {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableEntity(final Long id, final Long tableGroupId, final int numberOfGuests,
                            final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableEntity() {
    }

    public static OrderTableEntity from(final OrderTable orderTable) {
        return new OrderTableEntity(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests().getValue(), orderTable.isEmpty());
    }

    public static OrderTableEntity of(final OrderTable orderTable, final boolean empty) {
        return new OrderTableEntity(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests().getValue(), empty);
    }

    public OrderTable toOrderTable() {
        return new OrderTable(id, tableGroupId, NumberOfGuests.from(numberOfGuests), empty);
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
