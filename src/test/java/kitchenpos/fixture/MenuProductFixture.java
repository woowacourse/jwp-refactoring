package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴상품_망고_2개() {
        final var menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        return menuProduct;
    }

    public static MenuProduct 메뉴상품_치킨_1개() {
        final var menuProduct = new MenuProduct();
        menuProduct.setProductId(2L);
        menuProduct.setQuantity(1L);
        return menuProduct;
    }
}
