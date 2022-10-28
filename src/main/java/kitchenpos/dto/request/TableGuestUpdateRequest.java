package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class TableGuestUpdateRequest {

    private int numberOfGuests;

    private TableGuestUpdateRequest() {
    }

    public TableGuestUpdateRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toUpdateEntity(OrderTable orderTable) {
        return new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), numberOfGuests, orderTable.isEmpty());
    }
}
