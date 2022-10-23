package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class CreateOrderLineItemDto {

    private final Long menuId;
    private final Integer quantity;

    public CreateOrderLineItemDto(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
