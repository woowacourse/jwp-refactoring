package kitchenpos.ui.dto;

public class CreateTableRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    protected CreateTableRequest() {
    }

    public CreateTableRequest(final Integer numberOfGuests, final Boolean empty) {
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
