package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
        this(null, null);
    }

    public OrderRequest(
            Long orderTableId,
            List<OrderLineItemRequest> orderLineItemRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order toOrder(OrderTable orderTable, String orderStatus, LocalDateTime now) {
        return new Order(
                orderTable,
                orderStatus,
                now,
                orderLineItemRequests.stream()
                        .map(request -> request.toOrderLineItem(null))
                        .collect(Collectors.toList())
        );
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
