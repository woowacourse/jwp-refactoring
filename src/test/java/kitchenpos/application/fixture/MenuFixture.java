package kitchenpos.application.fixture;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;

import java.math.BigDecimal;
import java.util.List;

public abstract class MenuFixture {

    private MenuFixture() {
    }

    public static Menu menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> toAddMenuProducts) {
        final MenuProducts menuProducts = new MenuProducts();
        menuProducts.addAll(toAddMenuProducts);

        final Price menuPrice = new Price(price);

        return new Menu(name, menuPrice, menuGroup, menuProducts);
    }

    public static Menu menu(final String name, final Price menuPrice, final MenuGroup menuGroup, final List<MenuProduct> toAddMenuProducts) {
        final MenuProducts menuProducts = new MenuProducts();
        menuProducts.addAll(toAddMenuProducts);

        return new Menu(name, menuPrice, menuGroup, menuProducts);
    }
}
