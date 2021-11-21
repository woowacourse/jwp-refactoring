package kitchenpos.application.dto.request.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Menu menu) {
        return new OrderLineItem.OrderLineItemBuilder()
                .setMenu(menu)
                .setQuantity(quantity)
                .build();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
