package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    public Long getId() {
        return id;
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
