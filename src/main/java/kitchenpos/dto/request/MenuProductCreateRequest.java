package kitchenpos.dto.request;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductCreateRequest {

    private Long productId;
    private long quantity;

    private MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return MenuProduct.createEntity(null, menuId, productId, quantity);
    }
}
