package kitchenpos.table.dto;

public class CreateOrderTableDto {

    private final int numberOfGuests;
    private final boolean empty;

    public CreateOrderTableDto(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
