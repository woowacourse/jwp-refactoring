package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemCreateRequest {

    @NotNull
    private Long menuId;

    @NotNull
    private Integer quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
