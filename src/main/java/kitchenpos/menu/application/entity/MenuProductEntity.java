package kitchenpos.menu.application.entity;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductEntity {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductEntity(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductEntity(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity);
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

    public static MenuProductEntity of(final Long menuId, final MenuProduct menuProduct) {
        return new MenuProductEntity(menuProduct.getSeq(), menuId, menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(seq, productId, quantity);
    }
}
