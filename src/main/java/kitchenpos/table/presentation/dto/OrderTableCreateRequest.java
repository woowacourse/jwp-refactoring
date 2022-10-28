package kitchenpos.table.presentation.dto;

import kitchenpos.table.application.dto.OrderTableSaveRequest;

public class OrderTableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableSaveRequest toRequest() {
        return new OrderTableSaveRequest(numberOfGuests, empty);
    }
}
