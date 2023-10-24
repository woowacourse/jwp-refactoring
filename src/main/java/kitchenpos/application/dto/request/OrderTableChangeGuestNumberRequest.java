package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class OrderTableChangeGuestNumberRequest {

    private final int numberOfGuests;

    @JsonCreator
    public OrderTableChangeGuestNumberRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTableChangeGuestNumberRequest)) return false;
        OrderTableChangeGuestNumberRequest request = (OrderTableChangeGuestNumberRequest) o;
        return numberOfGuests == request.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}
