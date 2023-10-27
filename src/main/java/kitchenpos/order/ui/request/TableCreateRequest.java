package kitchenpos.order.ui.request;

public class TableCreateRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public TableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
