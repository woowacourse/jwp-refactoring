package kitchenpos.ui.dto;

public class TableGuestNumberRequest {

    private Integer numberOfGuests;

    protected TableGuestNumberRequest() {
    }

    public TableGuestNumberRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
