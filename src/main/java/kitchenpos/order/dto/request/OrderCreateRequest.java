package kitchenpos.order.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderLineItemQuantityDto;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItemQuantityDto> toOrderLineItemQuantities() {
        return orderLineItems.stream()
                .map(this::convertToOrderLineItemQuantity)
                .collect(Collectors.toList());
    }

    private OrderLineItemQuantityDto convertToOrderLineItemQuantity(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItemQuantityDto(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
