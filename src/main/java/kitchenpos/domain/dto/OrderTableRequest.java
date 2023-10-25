package kitchenpos.domain.dto;

public class OrderTableRequest {

    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
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
