package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableChangeRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableChangeRequest() {
    }

    private OrderTableChangeRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableChangeRequest of(OrderTable orderTable) {
        return new OrderTableChangeRequest(null, orderTable.getNumberOfGuests(),
            orderTable.isEmpty());
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

    public OrderTable toEntity(TableGroup tableGroup) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}
