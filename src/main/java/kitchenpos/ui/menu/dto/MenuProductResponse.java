package kitchenpos.ui.menu.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(final long seq, final long menuId, final long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
