package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixtures {

    public static final Menu generateMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    public static final Menu generateMenu(final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        return generateMenu(null, name, price, menuGroupId, menuProducts);
    }

    public static final Menu generateMenu(final Long id, final Menu menu) {
        return generateMenu(id, menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static final Menu generateMenu(final Long id,
                                          final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
