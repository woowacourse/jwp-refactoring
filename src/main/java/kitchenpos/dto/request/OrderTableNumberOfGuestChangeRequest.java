package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableNumberOfGuestChangeRequest {

    private int numberOfGuests;

    private OrderTableNumberOfGuestChangeRequest() {
    }

    public OrderTable toOrderTable() {
        return new OrderTable(null, null, numberOfGuests, false);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
