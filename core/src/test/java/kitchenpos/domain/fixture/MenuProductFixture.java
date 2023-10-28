package kitchenpos.domain.fixture;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;

public abstract class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct menuProduct(final Long productId, final long price) {
        return new MenuProduct(productId, price);
    }

    public static MenuProduct menuProduct(final Menu menu, final Long productId, final long price) {
        return new MenuProduct(menu, productId, price);
    }
}
