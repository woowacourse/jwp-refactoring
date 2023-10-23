package kitchenpos.ui.request;

public class TableUpdateNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public TableUpdateNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public TableUpdateNumberOfGuestsRequest() {
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
