package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableCreateRequest {

    private final int numberOfGuests;

    public OrderTableCreateRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable toEntity() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return orderTable;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
