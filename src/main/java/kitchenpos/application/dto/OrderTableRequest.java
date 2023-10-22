package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuest;

    private OrderTableRequest() {
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuest);
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }
}
