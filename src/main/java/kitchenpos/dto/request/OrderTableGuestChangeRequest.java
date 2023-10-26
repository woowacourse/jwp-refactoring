package kitchenpos.dto.request;

public class OrderTableGuestChangeRequest {

    private final int numbersOfGuest;

    public OrderTableGuestChangeRequest(int numbersOfGuest) {
        this.numbersOfGuest = numbersOfGuest;
    }

    public int getNumbersOfGuest() {
        return numbersOfGuest;
    }
}
