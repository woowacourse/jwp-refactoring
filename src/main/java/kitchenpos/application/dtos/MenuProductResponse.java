package kitchenpos.application.dtos;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        this.productId = menuProduct.getProductId();
        this.quantity = menuProduct.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
