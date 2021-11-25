package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toEntity(OrderTable orderTable) {
        return new Order(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());
    }

    public List<Long> getMenuIds() {
        return orderLineItemRequests.stream()
                                    .map(OrderLineItemRequest::getMenuId)
                                    .collect(Collectors.toList());
    }
}
