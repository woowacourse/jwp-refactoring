package kitchenpos.application.dto.ordertable;

public class ChangeOrderTableNumberOfGuestsCommand {

    private final Long id;
    private final int numberOfGuests;

    public ChangeOrderTableNumberOfGuestsCommand(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
    }

    public Long id() {
        return id;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }
}
