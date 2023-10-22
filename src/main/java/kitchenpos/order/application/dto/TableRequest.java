package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderTable;

public class TableRequest {
    private Integer numberOfGuests;
    private Boolean empty;

    public TableRequest() {
    }

    public TableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
