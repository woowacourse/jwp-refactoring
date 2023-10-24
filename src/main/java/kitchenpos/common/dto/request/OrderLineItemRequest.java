package kitchenpos.common.dto.request;

import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull
    private final Long menuId;
    @NotNull
    private final Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

}
