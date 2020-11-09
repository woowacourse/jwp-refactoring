package kitchenpos.application.command;

import javax.validation.constraints.Min;

public class ChangeNumberOfOrderTableGuestsCommand {
    @Min(0)
    private int numberOfGuests;

    private ChangeNumberOfOrderTableGuestsCommand() {
    }

    public ChangeNumberOfOrderTableGuestsCommand(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
