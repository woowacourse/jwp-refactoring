package kitchenpos.application.dto;

import kitchenpos.domain.table.OrderTable;

public class TableDto {

    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    private TableDto() {
    }

    public TableDto(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableDto of(OrderTable orderTable) {
        return new TableDto(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
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
