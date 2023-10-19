package kitchenpos.application.dto.request;

public class OrderTableChangeEmptyRequest {
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
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
