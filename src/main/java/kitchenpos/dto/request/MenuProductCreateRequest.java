package kitchenpos.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {
    private Long productId;
    private int quantity;

    public MenuProductCreateRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(menuId, this.productId, this.quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
