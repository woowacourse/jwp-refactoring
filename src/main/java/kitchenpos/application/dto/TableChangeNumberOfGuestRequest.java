package kitchenpos.application.dto;

public class TableChangeNumberOfGuestRequest {

    private final Integer numberOfGuests;

    public TableChangeNumberOfGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
