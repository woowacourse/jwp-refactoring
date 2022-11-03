package kitchenpos.table.ui.dto;

import java.util.Objects;

public class OrderTableIdRequest {

    private Long id;

    private OrderTableIdRequest() {
    }

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTableIdRequest that = (OrderTableIdRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
