package kitchenpos.application.dto.response;

import java.util.Objects;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = getTableGroupId(orderTable.getTableGroup());
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public Long getTableGroupId(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return 0L;
        }
        return tableGroup.getId();
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
