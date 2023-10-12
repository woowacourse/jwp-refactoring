package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public final class MenuProductFixture {

    public static MenuProduct 메뉴_상품_생성(final Long productId) {
        final MenuProduct menuProduct = new MenuProduct();

        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1);

        return menuProduct;
    }

    private MenuProductFixture() {
    }
}
