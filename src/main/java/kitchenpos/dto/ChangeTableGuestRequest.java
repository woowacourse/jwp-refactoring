package kitchenpos.dto;

public class ChangeTableGuestRequest {

    private Integer numberOfGuests;

    public ChangeTableGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
