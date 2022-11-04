package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse(final Long menuId, final MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menuId;
        this.productId = menuProduct.getProductId();
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
