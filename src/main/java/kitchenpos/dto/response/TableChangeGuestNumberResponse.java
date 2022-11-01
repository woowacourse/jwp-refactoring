package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderTable;

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
