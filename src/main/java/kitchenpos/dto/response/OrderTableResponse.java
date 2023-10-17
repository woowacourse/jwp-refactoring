package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final Boolean empty;

    private OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        if(orderTable.getTableGroup() == null){
            return new OrderTableResponse(orderTable.getId(), null,
                    orderTable.getNumberOfGuests(), orderTable.isEmpty());
        }
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroup().getId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
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

    public Boolean isEmpty() {
        return empty;
    }
}
