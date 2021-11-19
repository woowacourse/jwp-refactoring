package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class TableRequest {

    private Long id;
    private Integer numberOfGuest;
    private Boolean empty;

    public TableRequest() {
    }

    public TableRequest(Long id, Integer numberOfGuest, Boolean empty) {
        this.id = id;
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public Long getId() {
        return id;
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
