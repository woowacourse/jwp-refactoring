package kitchenpos.tablegroup.ui.request;

import java.util.Objects;

public class OrderTableIdRequest {
    private final Long id;

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableIdRequest that = (OrderTableIdRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTableIdRequest{" +
                "id=" + id +
                '}';
    }
}
