package kitchenpos.application.dto;

public class TableChangeNumberOfGuestRequest {

    private final Integer numberOfGuests;

    private TableChangeNumberOfGuestRequest() {
        this(null);
    }

    public TableChangeNumberOfGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
