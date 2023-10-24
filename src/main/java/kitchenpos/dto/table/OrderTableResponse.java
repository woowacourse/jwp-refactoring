package kitchenpos.dto.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class OrderTableResponse {

    private final Long id;
    private final TableGroup tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(Long id, TableGroup tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroup(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
