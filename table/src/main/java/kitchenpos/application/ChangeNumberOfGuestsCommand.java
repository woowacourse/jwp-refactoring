package kitchenpos.application;

public class ChangeNumberOfGuestsCommand {
    private Long orderTableId;
    private int numberOfGuests;

    public ChangeNumberOfGuestsCommand(final Long orderTableId, final int numberOfGuests) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

}
