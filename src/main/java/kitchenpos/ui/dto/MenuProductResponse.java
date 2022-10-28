package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(long seq, long menuId, long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity()
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
