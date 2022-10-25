package kitchenpos.domain.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductFixture() {
    }

    public static MenuProductFixture 후라이드() {
        return new MenuProductFixture();
    }

    public MenuProductFixture 상품_아이디(final Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductFixture 수량(final int quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
