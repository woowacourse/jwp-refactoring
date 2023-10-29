package table.dto;

public class CreateOrderTableRequest {
    private int numberOfGuests;
    private boolean orderable;


    private CreateOrderTableRequest(int numberOfGuests, boolean orderable) {
        this.numberOfGuests = numberOfGuests;
        this.orderable = orderable;
    }

    public CreateOrderTableRequest() {
    }

    public static CreateOrderTableRequest of(int numberOfGuests, boolean orderable) {
        return new CreateOrderTableRequest(numberOfGuests, orderable);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isOrderable() {
        return orderable;
    }
}
