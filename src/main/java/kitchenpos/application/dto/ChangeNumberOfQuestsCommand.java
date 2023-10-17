package kitchenpos.application.dto;

public class ChangeNumberOfQuestsCommand {
    private Long orderTableId;
    private int numberOfGuests;

    public ChangeNumberOfQuestsCommand(final Long orderTableId, final int numberOfGuests) {
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
