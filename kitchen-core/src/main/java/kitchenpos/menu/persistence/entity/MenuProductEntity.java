package kitchenpos.menu.persistence.entity;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

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
        return new MenuProductEntity(menuProduct.getSeq(), menuId, menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public MenuProduct toMenuProduct(final Product product) {
        return new MenuProduct(seq, product, quantity);
    }
}
