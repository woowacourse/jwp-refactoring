package kitchenpos.dto.request;

public class OrderTableGuestRequestDto {

    private int numberOfGuests;

    private OrderTableGuestRequestDto() {
    }

    public OrderTableGuestRequestDto(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
