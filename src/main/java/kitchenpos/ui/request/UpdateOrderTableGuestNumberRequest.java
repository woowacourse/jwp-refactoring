package kitchenpos.ui.request;

public class UpdateOrderTableGuestNumberRequest {

    private Integer numberOfGuests;

    public UpdateOrderTableGuestNumberRequest() {
    }

    public UpdateOrderTableGuestNumberRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
