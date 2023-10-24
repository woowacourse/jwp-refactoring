package kitchenpos.dto.table;

public class ChangeNumberOfGuestsRequest {
    private final long orderTableId;
    private final int numberOfGuests;

    private ChangeNumberOfGuestsRequest(final long orderTableId, final int numberOfGuests) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public static ChangeNumberOfGuestsRequest of(final long orderTableId, final int numberOfGuests) {
        return new ChangeNumberOfGuestsRequest(orderTableId, numberOfGuests);
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
