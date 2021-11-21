package kitchenpos.ui.dto.request.table;

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
