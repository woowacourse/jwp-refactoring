package kitchenpos.application.dto;

import java.math.BigDecimal;

public class CreateOrderLineItemDto {

    private Long menuId;
    private Long quantity;

    public CreateOrderLineItemDto(Long menuId, Long quantity) {
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
