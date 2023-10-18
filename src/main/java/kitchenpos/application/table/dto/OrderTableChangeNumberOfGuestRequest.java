package kitchenpos.application.table.dto;

public class OrderTableChangeNumberOfGuestRequest {

    private Integer numberOfGuests;

    private OrderTableChangeNumberOfGuestRequest() {
    }

    public OrderTableChangeNumberOfGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
