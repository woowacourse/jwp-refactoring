package kitchenpos.table.application.request;

public class ChangeNumOfTableGuestsRequest {

    private final Long orderTableId;
    private final int numberOfGuests;

    public ChangeNumOfTableGuestsRequest(Long orderTableId, int numberOfGuests) {
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
