package kitchenpos.ui.dto.table;

import kitchenpos.domain.OrderTable;

public class CreateOrderTableRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public CreateOrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    public boolean isEmpty() {
        return empty;
    }
}
