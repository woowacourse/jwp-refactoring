package kitchenpos.ordertable.ui.request;

import java.util.Objects;

public class UpdateOrderTableEmptyRequest {
    private final boolean empty;

    public UpdateOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UpdateOrderTableEmptyRequest that = (UpdateOrderTableEmptyRequest) o;
        return empty == that.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empty);
    }

    @Override
    public String toString() {
        return "UpdateOrderTableEmptyRequest{" +
                "empty=" + empty +
                '}';
    }
}
