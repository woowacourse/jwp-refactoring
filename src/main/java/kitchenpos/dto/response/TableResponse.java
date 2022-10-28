package kitchenpos.dto.response;

import java.util.Objects;
import kitchenpos.domain.OrderTable;

public class TableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public TableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse from(final OrderTable orderTable) {
        if (Objects.isNull(orderTable.getTableGroup())) {
            return new TableResponse(orderTable.getId(), null, orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }
        return new TableResponse(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
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
