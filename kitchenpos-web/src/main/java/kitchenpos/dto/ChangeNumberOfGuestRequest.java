package kitchenpos.dto;

public class ChangeNumberOfGuestRequest {
    private int numberOfGuest;

    public ChangeNumberOfGuestRequest() {
    }

    public ChangeNumberOfGuestRequest(final int numberOfGuest) {
        this.numberOfGuest = numberOfGuest;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }
}
