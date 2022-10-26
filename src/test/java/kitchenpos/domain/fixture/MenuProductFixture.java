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

    public static MenuProduct 상품_하나(final Long productId) {
        return 메뉴_그룹()
            .상품_아이디(productId)
            .수량(1)
            .build();
    }

    private static MenuProductFixture 메뉴_그룹() {
        return new MenuProductFixture();
    }

    private MenuProductFixture 상품_아이디(final Long productId) {
        this.productId = productId;
        return this;
    }

    private MenuProductFixture 수량(final int quantity) {
        this.quantity = quantity;
        return this;
    }

    private MenuProduct build() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
