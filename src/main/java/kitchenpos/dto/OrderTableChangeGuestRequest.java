package kitchenpos.dto;

public class OrderTableChangeGuestRequest {

    private Integer numberOfGuests;

    public OrderTableChangeGuestRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
