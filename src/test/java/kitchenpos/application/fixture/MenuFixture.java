package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;

import java.math.BigDecimal;
import java.util.List;

public abstract class MenuFixture {

    private MenuFixture() {
    }

    public static Menu menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, new MenuProducts(menuProducts));
    }
}
