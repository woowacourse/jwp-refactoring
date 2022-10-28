package kitchenpos.ui;

public class TableChangeNumberOfGuestsRequest {

    private Integer numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {}

    public TableChangeNumberOfGuestsRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
