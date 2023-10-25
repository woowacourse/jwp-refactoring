package kitchenpos.ui.dto.order;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemDto(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(final Order order, final Menu menu) {
        return new OrderLineItem(order, menu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
