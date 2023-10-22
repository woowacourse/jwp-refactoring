package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(final Long productId, final int quantity, final Long menuId) {
        return new MenuProduct(menuId, productId, quantity);
    }
}
