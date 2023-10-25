package kitchenpos.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Objects;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    @JsonCreator
    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCreateRequest)) return false;
        OrderCreateRequest that = (OrderCreateRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItems);
    }
}
