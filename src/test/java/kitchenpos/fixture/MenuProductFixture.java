package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(final Long seq, final Long menuId, final Long productId, final long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
