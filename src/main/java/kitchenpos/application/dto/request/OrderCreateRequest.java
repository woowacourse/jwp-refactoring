package kitchenpos.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId,
            List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Order toEntity(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order.OrderBuilder()
                .setOrderTable(orderTable)
                .setOrderLineItems(orderLineItems)
                .build();
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
}
