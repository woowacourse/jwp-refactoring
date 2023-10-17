package kitchenpos.dto;

public class TableCreationRequest {

    private final Integer numberOfGuests;
    private final Boolean empty;

    public TableCreationRequest(Integer numberOfGuests, Boolean empty) {
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
