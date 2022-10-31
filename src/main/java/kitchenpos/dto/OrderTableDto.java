package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableDto {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableDto() {
    }

    public OrderTableDto(final OrderTable orderTable) {
        this.id = orderTable.getId();
        this.tableGroupId = orderTable.getTableGroupId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public OrderTable toEntity() {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
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

    public boolean isEmpty() {
        return empty;
    }
}
