package kitchenpos.ui.dto;

public class GuestChangeRequest {

    private Integer numberOfGuests;

    public GuestChangeRequest() {
    }

    public GuestChangeRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
