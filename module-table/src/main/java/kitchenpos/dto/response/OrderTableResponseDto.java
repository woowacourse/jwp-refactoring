package kitchenpos.dto.response;

public class OrderTableResponseDto {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private Boolean empty;

    private OrderTableResponseDto() {
    }

    public OrderTableResponseDto(
        Long id,
        Long tableGroupId,
        int numberOfGuests,
        Boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public Boolean getEmpty() {
        return empty;
    }
}
