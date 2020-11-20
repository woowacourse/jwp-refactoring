package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    @NotNull
    private final Long menuId;

    @Positive
    private final int quantity;

    public OrderLineItemCreateRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Long orderId) {
        return new OrderLineItem(null, orderId, menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
