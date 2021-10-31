package kitchenpos.application.dto;

public class TableRequest {
    private final Integer numberOfGuests;
    private final Boolean empty;

    public TableRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
