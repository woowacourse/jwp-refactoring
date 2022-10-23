package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private Integer quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
