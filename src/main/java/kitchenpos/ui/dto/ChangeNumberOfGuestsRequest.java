package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public OrderTable toEntity() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return orderTable;
    }
}
