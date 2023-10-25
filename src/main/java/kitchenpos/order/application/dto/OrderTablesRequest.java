package kitchenpos.order.application.dto;

import java.util.List;
import java.util.Objects;

public class OrderTablesRequest {

    private List<Long> orderTableIds;

    private OrderTablesRequest() {
    }

    public OrderTablesRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTablesRequest that = (OrderTablesRequest) o;
        return Objects.equals(orderTableIds, that.orderTableIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableIds);
    }
}
