package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴상품_망고_2개() {
        return new MenuProduct(MenuFixture.메뉴_망고치킨_17000원_신메뉴(), ProductFixture.상품_망고_1000원(), 2L);
    }

    public static MenuProduct 메뉴상품_치킨_1개() {
        return new MenuProduct(MenuFixture.메뉴_망고치킨_17000원_신메뉴(), ProductFixture.상품_치킨_15000원(), 1L);
    }
}
