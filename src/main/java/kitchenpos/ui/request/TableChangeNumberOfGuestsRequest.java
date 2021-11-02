package kitchenpos.ui.request;

public class TableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
