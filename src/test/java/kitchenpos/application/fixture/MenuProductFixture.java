package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;

public abstract class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct menuProduct(final Product product, final long price) {
        return new MenuProduct(product, price);
    }

    public static MenuProduct menuProduct(final Menu menu, final Product product, final long price) {
        return new MenuProduct(menu, product, price);
    }
}
