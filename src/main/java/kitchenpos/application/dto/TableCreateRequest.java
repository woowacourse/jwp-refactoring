package kitchenpos.application.dto;

public class TableCreateRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public TableCreateRequest(final Integer numberOfGuest, final Boolean empty) {
        this.numberOfGuests = numberOfGuest;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
