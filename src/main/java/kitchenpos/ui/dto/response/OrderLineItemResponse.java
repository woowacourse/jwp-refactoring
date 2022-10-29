package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.OrderLineItemDto;

public class OrderLineItemResponse {

    private Long id;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long id, final Long orderId, final Long menuId, final long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
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

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
