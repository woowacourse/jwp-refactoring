package kitchenpos.ordertable.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class OrderTableChangeEmptyRequest {

    private final boolean empty;

    @JsonCreator
    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTableChangeEmptyRequest)) return false;
        OrderTableChangeEmptyRequest request = (OrderTableChangeEmptyRequest) o;
        return empty == request.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
