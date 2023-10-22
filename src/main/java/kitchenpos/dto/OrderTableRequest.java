package kitchenpos.dto;

public class OrderTableRequest {

    private Integer numberOfGuests;

    public OrderTableRequest(final Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuest() {
        return numberOfGuests;
    }
}
