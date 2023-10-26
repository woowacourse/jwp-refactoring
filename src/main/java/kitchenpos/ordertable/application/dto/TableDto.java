package kitchenpos.ordertable.application.dto;

import java.util.Objects;
import kitchenpos.ordertable.domain.OrderTable;

public class TableDto {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public TableDto(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableDto toDto(final OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new TableDto(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }
        return new TableDto(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
