package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }
}
