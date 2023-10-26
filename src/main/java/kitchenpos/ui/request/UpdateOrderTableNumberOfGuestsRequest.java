package kitchenpos.ui.request;

import java.util.Objects;

public class UpdateOrderTableNumberOfGuestsRequest {
    private final int numberOfGuests;

    public UpdateOrderTableNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UpdateOrderTableNumberOfGuestsRequest that = (UpdateOrderTableNumberOfGuestsRequest) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public String toString() {
        return "UpdateOrderTableNumberOfGuestsRequest{" +
                "numberOfGuests=" + numberOfGuests +
                '}';
    }
}
