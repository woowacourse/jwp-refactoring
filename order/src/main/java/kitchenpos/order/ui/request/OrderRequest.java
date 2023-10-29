package kitchenpos.order.ui.request;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderRequest {
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(final Long orderTableId,
                        final List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public void validate() {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public List<Long> getMenuIds() {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderRequest that = (OrderRequest) o;
        return Objects.equals(orderTableId, that.orderTableId)
                && Objects.equals(orderLineItemRequests, that.orderLineItemRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableId, orderLineItemRequests);
    }
}
