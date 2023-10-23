package kitchenpos.ui.request;

import kitchenpos.domain.OrderTable;

public class TableCreateRequest {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final boolean empty;

    public TableCreateRequest(final Long id, final Long tableGroupId, final Integer numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(
                id,
                tableGroupId,
                numberOfGuests,
                empty
        );
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
