package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductSaveRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductSaveRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
