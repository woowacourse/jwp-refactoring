package kitchenpos.application.command;

import javax.validation.constraints.Min;

import kitchenpos.domain.model.ordertable.OrderTable;

public class CreateOrderTableCommand {
    @Min(0)
    private int numberOfGuests;
    private boolean empty;

    private CreateOrderTableCommand() {
    }

    public CreateOrderTableCommand(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
