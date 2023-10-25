package kitchenpos.table.application.response;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableResponse {

    private Long id;
    private TableGroupResponse tableGroupResponse;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(
            Long id,
            TableGroupResponse tableGroupResponse,
            int numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroupResponse = tableGroupResponse;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                convertToTableGroupIfExist(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    private static TableGroupResponse convertToTableGroupIfExist(TableGroup tableGroup) {
        if (tableGroup == null) {
            return null;
        }
        return TableGroupResponse.from(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public TableGroupResponse getTableGroupResponse() {
        return tableGroupResponse;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
