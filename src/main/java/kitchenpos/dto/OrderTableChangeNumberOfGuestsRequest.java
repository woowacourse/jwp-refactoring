package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    protected OrderTableChangeNumberOfGuestsRequest() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests);
    }
}
