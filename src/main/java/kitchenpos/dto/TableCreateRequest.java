package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableCreateRequest {
    private final int numberOfGuests;
    private final Boolean empty;

    public TableCreateRequest(final int numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
