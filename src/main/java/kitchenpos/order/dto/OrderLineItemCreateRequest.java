package kitchenpos.order.dto;

import javax.validation.constraints.NotNull;

public class OrderLineItemCreateRequest {
    @NotNull
    private Long quantity;
    @NotNull
    private Long menuId;

    public OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long quantity, Long menuId) {
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}