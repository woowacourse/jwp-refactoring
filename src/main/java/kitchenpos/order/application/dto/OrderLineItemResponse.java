package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemResponse {

    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final String menuName;
    private final Long menuPrice;
    private final long quantity;

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, String menuName, Long menuPrice, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toResponse(OrderLineItem orderLineItem) {
        OrderMenu orderMenu = orderLineItem.getOrderMenu();
        return new OrderLineItemResponse(orderLineItem.getId(), orderLineItem.getOrderId(), orderMenu.getMenuId(),
            orderMenu.getName().getValue(), orderMenu.getPrice().getValue().longValue(), orderLineItem.getQuantity());
    }
}
