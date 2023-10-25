package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public enum MenuProductFixture {

    FRIED_CHICKEN_MENU_PRODUCT(1L, 1L, ProductFixture.FRIED_CHICKEN.id, 2L),
    SPICY_CHICKEN_MENU_PRODUCT(2L, 2L, ProductFixture.SPICY_CHICKEN.id, 1L);

    public final Long seq;
    public final Long menuId;
    public final Long productId;
    public final long quantity;

    MenuProductFixture(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
