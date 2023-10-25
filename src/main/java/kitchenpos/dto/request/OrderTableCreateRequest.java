package kitchenpos.dto.request;

public class OrderTableCreateRequest {

    private final Long tableGroupId;
    private final int numberOfGuests;

    public OrderTableCreateRequest(Long tableGroupId, int numberOfGuests) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

}
