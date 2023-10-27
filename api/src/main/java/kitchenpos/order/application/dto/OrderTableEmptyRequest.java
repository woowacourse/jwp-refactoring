package kitchenpos.order.application.dto;

import java.util.Objects;

public class OrderTableEmptyRequest {

    private boolean empty;

    private OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableEmptyRequest that = (OrderTableEmptyRequest) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }
}
