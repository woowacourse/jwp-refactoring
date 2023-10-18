package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public abstract class MenuFixture {

    private MenuFixture() {
    }

    public static Menu menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }
}
