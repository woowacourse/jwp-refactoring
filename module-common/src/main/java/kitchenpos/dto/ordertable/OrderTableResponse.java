package kitchenpos.dto.ordertable;

import kitchenpos.domain.ordertable.OrderTable;

public class OrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    public OrderTableResponse(
        Long id,
        Long tableGroupId,
        Integer numberOfGuests,
        Boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(OrderTable orderTable) {
        this(
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

    public boolean isEmpty() {
        return empty;
    }
}
