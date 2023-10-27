package kitchenpos.order.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    @JsonCreator
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
