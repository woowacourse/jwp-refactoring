package kitchenpos.dto.table;

public class CreateOrderTableRequest {
    private final int numberOfGuests;
    private final boolean orderable;


    private CreateOrderTableRequest(int numberOfGuests, boolean orderable) {
        this.numberOfGuests = numberOfGuests;
        this.orderable = orderable;
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
