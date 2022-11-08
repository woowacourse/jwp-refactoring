package kitchenpos.ordertable.application.dto;

public class OrderTableChangeNumberOfGuestRequest {

    private final int numberOfGuests;

    public OrderTableChangeNumberOfGuestRequest(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("인원은 음수일 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
