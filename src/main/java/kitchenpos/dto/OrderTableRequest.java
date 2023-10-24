package kitchenpos.dto;

public class OrderTableRequest {
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest(final Long tableGroupId, final Integer numberOfGuests, final Boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
