package kitchenpos.ui.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductResponse(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getMenu().getId(), menuProduct.getProduct().getId(),
            menuProduct.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
