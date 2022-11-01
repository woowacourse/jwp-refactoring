package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateMenuProductDto;

public class CreateMenuProductRequest {

    private Long productId;
    private Long quantity;

    protected CreateMenuProductRequest() {
    }

    public CreateMenuProductRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CreateMenuProductDto toCreateMenuProductDto() {
        return new CreateMenuProductDto(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
