package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createMenu(final String name,
                                  final Long price,
                                  final Long menuGroupId,
                                  final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(menuProducts);
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    public static Menu createMenu(final Long id, final String name, final Long price, final Long menuGroupId) {
        final Menu menu = createMenu(name, price, menuGroupId, Collections.emptyList());
        menu.setId(id);

        return menu;
    }
}
