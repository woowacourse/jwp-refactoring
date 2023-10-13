package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_10개_생성(final Long menuId, final Long productId) {
        return new MenuProduct(null, menuId, productId, 10);
    }
}
