package kitchenpos.menu.application.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        this.seq = menuProduct.getId();
        this.menuId = menuProduct.getMenu().getId();
        this.productId = menuProduct.getProduct().getId();
        this.quantity = menuProduct.getQuantity();
    }

    public Long getSeq() {
        return seq;
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
