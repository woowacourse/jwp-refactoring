package kitchenpos.application.dto.request;

public class UpdateOrderTableGuests {

    private int numberOfGuests;

    public UpdateOrderTableGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
