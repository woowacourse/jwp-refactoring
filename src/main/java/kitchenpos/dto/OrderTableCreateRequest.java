package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableCreateRequest {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    private OrderTableCreateRequest(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableCreateRequest of(OrderTable orderTable) {
        return new OrderTableCreateRequest(null, orderTable.getNumberOfGuests(),
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
        return OrderTable.of(tableGroup, numberOfGuests, empty);
    }
}
