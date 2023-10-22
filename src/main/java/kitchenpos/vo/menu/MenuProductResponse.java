package kitchenpos.vo.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long ProductId;
    private final long quantity;

    private MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.ProductId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return ProductId;
    }

    public long getQuantity() {
        return quantity;
    }
}
