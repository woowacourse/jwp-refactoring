package kitchenpos.dto.response;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
                convertToResponseIfExist(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    private static TableGroupResponse convertToResponseIfExist(TableGroup tableGroup) {
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
