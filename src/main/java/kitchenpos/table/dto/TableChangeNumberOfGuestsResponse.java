package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class TableChangeNumberOfGuestsResponse {
    private int numberOfGuests;

    private TableChangeNumberOfGuestsResponse() {
    }

    public TableChangeNumberOfGuestsResponse(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static TableChangeNumberOfGuestsResponse from(final OrderTable orderTable) {
        return new TableChangeNumberOfGuestsResponse(orderTable.getNumberOfGuests());
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
