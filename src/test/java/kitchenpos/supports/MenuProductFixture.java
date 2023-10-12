package kitchenpos.supports;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    private Long seq = null;
    private Long menuId = null;
    private Long productId = 2L;
    private long quantity = 3L;

    private MenuProductFixture() {
    }

    public static MenuProductFixture fixture() {
        return new MenuProductFixture();
    }

    public MenuProductFixture seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductFixture menuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuProductFixture productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductFixture quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
