package kitchenpos.ui.dto.order;

import javax.validation.constraints.NotNull;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Order order, Menu menu) {
        return new OrderLineItem(order, menu, quantity);
    }


    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
