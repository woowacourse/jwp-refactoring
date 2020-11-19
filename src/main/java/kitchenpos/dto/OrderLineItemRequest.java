package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest from(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toOrderLineItem(Order order, Menu menu) {
        return new OrderLineItem(null, order, menu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
