package kitchenpos.table.ui.dto;

public class CreateOrderTableRequest {

    private int numberOfGuests;

    public CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
