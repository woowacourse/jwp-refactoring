package kitchenpos.domain.fixture;

import kitchenpos.common.Price;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProducts;

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
