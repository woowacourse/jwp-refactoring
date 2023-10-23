package kitchenpos.order.application.dto;

public class TableGuestRequest {

    private Integer numberOfGuests;

    public TableGuestRequest() {
    }

    public TableGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
