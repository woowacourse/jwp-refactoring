package kitchenpos.ui.dto.request;

import kitchenpos.application.dto.request.MenuProductRequestDto;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequestDto toDto() {
        return new MenuProductRequestDto(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
