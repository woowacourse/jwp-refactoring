package kitchenpos.order.service.dto;

public class TableRequest {

    private int numberOfGuests;
    private boolean empty;

    public TableRequest() {
    }

    public TableRequest(final boolean empty) {
        this.empty = empty;
    }

    public TableRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public TableRequest(final int numberOfGuests, final boolean empty) {
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
