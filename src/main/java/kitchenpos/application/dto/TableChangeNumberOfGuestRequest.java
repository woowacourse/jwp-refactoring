package kitchenpos.application.dto;

public class TableChangeNumberOfGuestRequest {

    private final Integer NumberOfGuests;

    public TableChangeNumberOfGuestRequest(Integer numberOfGuests) {
        NumberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return NumberOfGuests;
    }
}
