package kitchenpos.common.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long seq, Long menuId, Long productId) {
        return new MenuProduct(seq, menuId, productId, 1L);
    }

    public static MenuProduct 메뉴_상품(Long menuId, Long productId) {
        return new MenuProduct(menuId, productId, 1L);
    }

    public static MenuProduct 메뉴_상품(Long productId) {
        return new MenuProduct(productId, 1L);
    }
}
