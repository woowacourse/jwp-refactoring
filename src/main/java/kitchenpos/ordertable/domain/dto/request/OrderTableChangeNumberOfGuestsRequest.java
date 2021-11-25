package kitchenpos.ordertable.domain.dto.request;

import kitchenpos.ordertable.domain.OrderTable;

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
