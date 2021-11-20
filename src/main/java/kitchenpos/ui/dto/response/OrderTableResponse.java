package kitchenpos.ui.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private TableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(
            Long id,
            TableGroupResponse tableGroup,
            int numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse create(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                TableGroupResponse.create(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
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
