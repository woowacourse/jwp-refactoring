package kitchenpos.table.dto;

public class OrderTableRequest {

    private final int numberOfGuests;
    private final Boolean empty;

    public OrderTableRequest(final int numberOfGuests, final Boolean empty) {
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
