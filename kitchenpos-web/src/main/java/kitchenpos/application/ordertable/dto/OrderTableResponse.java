package kitchenpos.application.ordertable.dto;


import kitchenpos.domain.orertable.OrderTable;

public class OrderTableResponse {

    private final Long id;
    private final TableGroupResponse tableGroup;
    private final int numberOfGuests;
    private final boolean empty;

    private OrderTableResponse(final Long id, final TableGroupResponse tableGroup, final int numberOfGuests,
                               final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(final OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), TableGroupResponse.from(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public TableGroupResponse getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
