package kitchenpos.table.application.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableSaveRequest {

    private final int numberOfGuests;
    private final Boolean empty;

    public OrderTableSaveRequest(int numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
