package kitchenpos.ui.request;

public class TableRequest {

    private Integer numberOfGuests;
    private Boolean empty;

    public TableRequest() {
    }

    public TableRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(final Boolean empty) {
        this.empty = empty;
    }
}
