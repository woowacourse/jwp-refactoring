package kitchenpos.table.application.request;

public class TableGuestUpdateRequest {

    private Integer numberOfGuests;

    public TableGuestUpdateRequest() {
    }

    public TableGuestUpdateRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
