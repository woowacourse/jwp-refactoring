package kitchenpos.table.application.dto;

public class OrderTableRequestDto {

    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequestDto(Long tableGroupId, Integer numberOfGuests, Boolean empty) {
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
