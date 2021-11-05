package kitchenpos.dto.menuproduct;

import kitchenpos.domain.menuproduct.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        this(
            menuProduct.getSeq(),
            menuProduct.getMenuId(),
            menuProduct.getProductId(),
            menuProduct.getQuantityValue()
        );
    }

    public MenuProductResponse(
        Long seq,
        Long menuId,
        Long productId,
        long quantity
    ) {
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
