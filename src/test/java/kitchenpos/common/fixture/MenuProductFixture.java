package kitchenpos.common.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Quantity;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long seq, Long menuId, Long productId) {
        return new MenuProduct(seq, menuId, productId, Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품(Long menuId, Long productId) {
        return new MenuProduct(menuId, productId, Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품(Long productId) {
        return new MenuProduct(productId, Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품(Long productId, long quantity) {
        return new MenuProduct(productId, Quantity.valueOf(quantity));
    }
}
