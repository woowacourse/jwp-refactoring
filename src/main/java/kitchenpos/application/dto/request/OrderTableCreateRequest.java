package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class OrderTableCreateRequest {

    private final int numberOfGuests;
    private final boolean empty;

    @JsonCreator
    public OrderTableCreateRequest(final int numberOfGuests, final boolean empty) {
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
        if (!(o instanceof OrderTableCreateRequest)) return false;
        OrderTableCreateRequest request = (OrderTableCreateRequest) o;
        return numberOfGuests == request.numberOfGuests && empty == request.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests, empty);
    }
}
