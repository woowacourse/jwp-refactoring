package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private MenuProductResponse(final Long seq, final Long menuId,
                               final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        final Long seq = menuProduct.getSeq();
        final Long menuId = menuProduct.getMenu().getId();
        final Long productId = menuProduct.getProduct().getId();
        final long quantity = menuProduct.getQuantity();
        return new MenuProductResponse(seq, menuId, productId, quantity);
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
