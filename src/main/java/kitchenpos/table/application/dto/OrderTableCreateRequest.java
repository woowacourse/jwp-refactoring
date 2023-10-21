package kitchenpos.table.application.dto;

public final class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
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
