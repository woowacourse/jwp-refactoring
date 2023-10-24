package kitchenpos.ui.dto.table;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {

    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final Integer numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
