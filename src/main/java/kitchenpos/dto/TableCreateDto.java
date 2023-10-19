package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

public class TableCreateDto {

    private final int numberOfGuests;
    private final boolean empty;

    public TableCreateDto(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toDomain() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
