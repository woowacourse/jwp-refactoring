package kitchenpos.domain.order.service.dto;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class OrderTableResponse {

    private final Long id;

    private final Long tableGroupId;

    private final int numberOfGuests;

    private final boolean empty;

    public OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse toDto(final OrderTable orderTable) {
        final TableGroup tableGroup = orderTable.getTableGroup();

        if (tableGroup == null) {
            return new OrderTableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(), tableGroup.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
