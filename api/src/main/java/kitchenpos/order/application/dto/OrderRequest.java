package kitchenpos.order.application.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemsRequest;

    private OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemsRequest) {
        this.orderTableId = orderTableId;
        this.orderLineItemsRequest = orderLineItemsRequest;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemsRequest() {
        return orderLineItemsRequest;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderLineItemsRequest,
                that.orderLineItemsRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItemsRequest);
    }
}
