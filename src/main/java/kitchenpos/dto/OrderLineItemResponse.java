package kitchenpos.dto;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long id;
    private Long menuId;
    private Long quantity;

    public OrderLineItemResponse(Long id, Long menuId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toResponse(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
