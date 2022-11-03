package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemSaveRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemSaveRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Long orderId, OrderMenu orderMenu) {
        return new OrderLineItem(orderId, orderMenu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }
}
