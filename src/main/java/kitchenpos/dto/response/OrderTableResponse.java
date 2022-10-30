package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private TableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        id = orderTable.getId();

        if (orderTable.getTableGroup() != null) {
            tableGroup = new TableGroupResponse(orderTable.getTableGroup());
        }

        numberOfGuests = orderTable.getNumberOfGuests();
        empty = orderTable.isEmpty();
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

    public boolean getEmpty() {
        return empty;
    }

}
