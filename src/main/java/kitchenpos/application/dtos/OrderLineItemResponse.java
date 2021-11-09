package kitchenpos.application.dtos;

import kitchenpos.domain.OrderLineItem;
import lombok.Getter;

@Getter
public class OrderLineItemResponse {
    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final Long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.id = orderLineItem.getId();
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }
}
