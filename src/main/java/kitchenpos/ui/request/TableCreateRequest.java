package kitchenpos.ui.request;

public class TableCreateRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public TableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableCreateRequest() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
