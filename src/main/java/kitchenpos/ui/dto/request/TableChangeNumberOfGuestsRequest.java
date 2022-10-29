package kitchenpos.ui.dto.request;

public class TableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {}

    public TableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public String toString() {
        return "TableChangeNumberOfGuestsRequest{" +
                "numberOfGuests=" + numberOfGuests +
                '}';
    }
}
