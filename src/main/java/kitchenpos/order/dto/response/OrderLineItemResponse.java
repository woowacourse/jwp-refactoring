package kitchenpos.order.dto.response;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long menuId;
    private long quantity;

    private OrderLineItemResponse(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getMenu().getId(),
                orderLineItem.getQuantity()
        );
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
