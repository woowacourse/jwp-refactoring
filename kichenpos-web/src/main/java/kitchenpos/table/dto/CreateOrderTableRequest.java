package kitchenpos.table.dto;

public class CreateOrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public CreateOrderTableRequest() {
    }

    public CreateOrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public CreateOrderTableDto toCreateOrderTableDto() {
        return new CreateOrderTableDto(numberOfGuests, empty);
    }
}
