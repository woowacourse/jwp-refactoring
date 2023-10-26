package kitchenpos.order.application.dto;

import java.util.Objects;

public class OrderTableGuestRequest {

    private int numberOfGuests;

    private OrderTableGuestRequest() {
    }

    public OrderTableGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableGuestRequest that = (OrderTableGuestRequest) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
