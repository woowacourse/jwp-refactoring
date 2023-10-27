package kitchenpos.order.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderLineItemRequest {

    @JsonProperty("menuId")
    private Long menuId;
    @JsonProperty("quantity")
    private long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity;
    }
}
