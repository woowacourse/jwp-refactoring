package kitchenpos.table.ui.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableChangeNumberOfGuestsRequest {
    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toEntity() {
        return new OrderTable(null, numberOfGuests, false);
    }
}
