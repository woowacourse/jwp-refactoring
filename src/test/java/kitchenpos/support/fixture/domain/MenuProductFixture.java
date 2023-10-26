package kitchenpos.support.fixture.domain;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct getMenuProduct(final Long menuId, final Long productId, final Long quantity) {
        return new MenuProduct(null, menuId, productId, quantity);
    }
}
