package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableNumberOfGuestChangeRequest {

    private int numberOfGuests;

    private OrderTableNumberOfGuestChangeRequest() {
    }

    public OrderTable toOrderTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
