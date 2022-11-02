package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

public class OrderTableNumberOfGuestResponse {

    private final int numberOfGuests;

    private OrderTableNumberOfGuestResponse(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTableNumberOfGuestResponse from(OrderTable orderTable){
        return new OrderTableNumberOfGuestResponse(orderTable.getNumberOfGuests());
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
