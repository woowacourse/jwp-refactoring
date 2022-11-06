package kitchenpos.order.ui.dto.response;

import kitchenpos.order.application.dto.OrderLineItemDto;

public class OrderLineItemResponse {

    private Long id;
    private Long orderId;
    private Long orderedMenuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long id, final Long orderId, final Long orderedMenuId, final long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.orderedMenuId = orderedMenuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItemDto orderLineItemDto) {
        return new OrderLineItemResponse(orderLineItemDto.getId(),
                orderLineItemDto.getOrderId(),
                orderLineItemDto.getMenuId(),
                orderLineItemDto.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderedMenuId() {
        return orderedMenuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderedMenuId=" + orderedMenuId +
                ", quantity=" + quantity +
                '}';
    }
}
