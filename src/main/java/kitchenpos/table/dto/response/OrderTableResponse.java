package kitchenpos.table.dto.response;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean isEmpty;

    public OrderTableResponse() {
    }

    private OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            return new OrderTableResponse(
                    orderTable.getId(),
                    orderTable.getTableGroup().getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
            );
        }

        return new OrderTableResponse(
                orderTable.getId(),
                null,
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
