package kitchenpos.dto.table;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;

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
        if (orderTable.getTableGroup().isEmpty()) {
            return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isOrderable(), null);
        }
        final TableGroup tableGroup = orderTable.getTableGroup().get();
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isOrderable(), tableGroup.getId());
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
