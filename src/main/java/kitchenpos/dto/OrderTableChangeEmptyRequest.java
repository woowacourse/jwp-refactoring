package kitchenpos.dto;

public class OrderTableChangeEmptyRequest {

    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableChangeEmptyRequest(
            final Long tableGroupId,
            final int numberOfGuests,
            final boolean empty
    ) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
