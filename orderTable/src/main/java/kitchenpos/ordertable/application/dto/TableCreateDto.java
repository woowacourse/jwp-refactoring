package kitchenpos.ordertable.application.dto;

public class TableCreateDto {

    private final int numberOfGuests;
    private final boolean empty;

    public TableCreateDto(final int numberOfGuests, final boolean empty) {
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
