package kitchenpos.test.fixtures;

import kitchenpos.domain.MenuProduct;

public enum MenuProductFixtures {
    EMPTY(0L, 0L, 0L, 0L),
    BASIC(1L, 1L, 1L, 1L);

    private final long seq;
    private final long menuId;
    private final long productId;
    private final long quantity;

    MenuProductFixtures(final long seq, final long menuId, final long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct get() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
