package kitchenpos.application.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private final int numberOfGuest;

    public OrderTableRequest(final int numberOfGuest) {
        this.numberOfGuest = numberOfGuest;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuest);
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }
}
