package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {

    private Long id;
    private TableGroupResponse tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        id = orderTable.getId();
        tableGroup = new TableGroupResponse(orderTable.getTableGroup());
        numberOfGuests = orderTable.getNumberOfGuests();
        empty = orderTable.isEmpty();
    }
}
