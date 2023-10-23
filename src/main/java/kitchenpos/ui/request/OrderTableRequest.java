package kitchenpos.ui.request;

import java.util.Objects;

public class OrderTableRequest {
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableRequest that = (OrderTableRequest) o;
        return numberOfGuests == that.numberOfGuests
                && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId, numberOfGuests, empty);
    }

    @Override
    public String toString() {
        return "OrderTableRequest{" +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
