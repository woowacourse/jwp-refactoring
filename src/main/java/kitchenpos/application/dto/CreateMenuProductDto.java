package kitchenpos.application.dto;

import lombok.Getter;

@Getter
public class CreateMenuProductDto {

    private final Long productId;
    private final Integer quantity;

    public CreateMenuProductDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
