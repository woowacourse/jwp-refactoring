package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    private MenuProductRequest() {}

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(
          productId,
          quantity
        );
    }
}
