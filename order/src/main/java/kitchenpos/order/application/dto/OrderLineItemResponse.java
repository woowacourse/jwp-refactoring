package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;
    private final String menuName;
    private final Long menuPrice;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.id = orderLineItem.getId();
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
        this.menuName = orderLineItem.getMenuName();
        this.menuPrice = orderLineItem.getMenuPrice().longValue();
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

    public Long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public Long getMenuPrice() {
        return menuPrice;
    }
}
