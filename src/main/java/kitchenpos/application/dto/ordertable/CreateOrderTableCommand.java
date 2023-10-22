package kitchenpos.application.dto.ordertable;

public class CreateOrderTableCommand {

    private final int numberOfGuests;
    private final boolean empty;

    public CreateOrderTableCommand(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean empty() {
        return empty;
    }
}
