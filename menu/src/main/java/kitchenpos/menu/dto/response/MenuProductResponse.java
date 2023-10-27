package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private final Long menuId;
    private final Long productId;
    private final long quantity;
    private final Long seq;

    private MenuProductResponse(final Long menuId, final Long productId, final long quantity, final Long seq) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.seq = seq;
    }

    public static MenuProductResponse of(final Long menuId, final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuId,
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity(),
                menuProduct.getSeq()
        );
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

    public Long getSeq() {
        return seq;
    }
}
