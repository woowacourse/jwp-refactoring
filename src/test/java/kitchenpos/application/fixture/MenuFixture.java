package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createMenu(final String name,
                                  final Long price,
                                  final Long menuGroupId,
                                  final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroupId);
        menu.addMenuProducts(menuProducts);

        return menu;
    }

    public static Menu createMenu(final Long id, final String name, final Long price, final Long menuGroupId) {
        return new Menu(id, name, BigDecimal.valueOf(price), menuGroupId);
    }
}
