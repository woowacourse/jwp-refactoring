package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public OrderTable toEntity(final OrderTable savedOrderTable) {
        return new OrderTable(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                numberOfGuests,
                empty
        );
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
