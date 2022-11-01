package kitchenpos.application.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(final Long seq, final Long menuId, final Long productId) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
    }

    public static MenuProductResponse createResponse(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId());
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
}
