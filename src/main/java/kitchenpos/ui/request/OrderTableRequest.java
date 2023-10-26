package kitchenpos.ui.request;

import java.util.Objects;

public class OrderTableRequest {
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(final int numberOfGuests,
                             final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
                && empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }

    @Override
    public String
    toString() {
        return "OrderTableRequest{" +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
