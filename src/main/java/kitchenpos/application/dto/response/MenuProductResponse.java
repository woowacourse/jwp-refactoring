package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

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
