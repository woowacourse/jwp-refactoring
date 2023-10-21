package kitchenpos.application.dto;

public class OrderTableUpdateGuestDto {

    private final Integer numberOfGuests;

    public OrderTableUpdateGuestDto(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
