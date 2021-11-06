package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class TableRequest {

    private Integer numberOfGuest;
    private Boolean empty;

    public TableRequest() {
    }

    public TableRequest(Integer numberOfGuest, Boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public Integer getNumberOfGuest() {
        return numberOfGuest;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(numberOfGuest, empty);
    }
}
