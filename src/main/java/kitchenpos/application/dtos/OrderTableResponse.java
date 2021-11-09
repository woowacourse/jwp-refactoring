package kitchenpos.application.dtos;

import kitchenpos.domain.OrderTable;

public class OrderTableResponse {
    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableResponse(OrderTable orderTable) {
        this.id = orderTable.getId();
        this.numberOfGuests = orderTable.getNumberOfGuests();
        this.empty = orderTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
