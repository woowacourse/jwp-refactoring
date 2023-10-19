package kitchenpos.test.fixtures;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public enum MenuFixtures {
    EMPTY("empty", new BigDecimal(0), 0L, Collections.emptyList()),
    BASIC("basic", new BigDecimal(1), 1L, List.of(MenuProductFixtures.BASIC.get()));

    private final String name;
    private final BigDecimal price;
    private final long groupId;
    private final List<MenuProduct> menuProducts;

    MenuFixtures(final String name, final BigDecimal price, final long groupId, final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.groupId = groupId;
        this.menuProducts = menuProducts;
    }

    public Menu get() {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(groupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
