package kitchenpos.dto.response;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse(final MenuProduct menuProduct) {
        this(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductResponse(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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
