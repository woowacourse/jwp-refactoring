package kitchenpos.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private final Long seq;
    private final Long productId;
    private final Long quantity;
    private final Long menuId;

    public MenuProductResponse(final Long seq, final Long productId, final Long quantity, final Long menuId) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity(),
                menuProduct.getMenuId());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }
}
