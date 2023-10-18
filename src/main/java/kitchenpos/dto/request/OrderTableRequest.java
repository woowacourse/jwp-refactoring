package kitchenpos.dto.request;

public class OrderTableRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableRequest() {
    }

    public OrderTableRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
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
