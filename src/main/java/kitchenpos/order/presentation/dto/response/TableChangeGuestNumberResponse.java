package kitchenpos.order.presentation.dto.response;

import kitchenpos.order.domain.OrderTable;

public class TableChangeGuestNumberResponse {

    private int numberOfGuests;

    private TableChangeGuestNumberResponse() {
    }

    public TableChangeGuestNumberResponse(OrderTable table) {
        numberOfGuests = table.getNumberOfGuests();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
