package kitchenpos.order.ui.request;

public class TableUpdateNumberOfGuestsRequest {

    private Integer numberOfGuests;

    public TableUpdateNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    protected TableUpdateNumberOfGuestsRequest() {
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
