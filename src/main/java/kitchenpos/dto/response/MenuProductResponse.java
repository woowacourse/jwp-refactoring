package kitchenpos.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(final Long seq, final Long menuId, final Long productId, final Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(), menuProduct.getQuantity());
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

    public Long getQuantity() {
        return quantity;
    }
}
