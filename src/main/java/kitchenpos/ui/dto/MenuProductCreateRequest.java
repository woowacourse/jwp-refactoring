package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {
    private Long productId;
    private int quantity;

    public MenuProductCreateRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(null, menuId, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
