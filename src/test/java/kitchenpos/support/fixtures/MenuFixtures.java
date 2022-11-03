package kitchenpos.support.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

public enum MenuFixtures {

    TWO_CHICKEN_COMBO("두마리 치킨 콤보", new MenuPrice(new BigDecimal(30_000)), 1L, new ArrayList<>());

    private final String name;
    private final MenuPrice menuPrice;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    MenuFixtures(final String name, final MenuPrice menuPrice, final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu create() {
        return new Menu(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public Menu createWithPrice(final BigDecimal price) {
        return new Menu(null, name, new MenuPrice(price), menuGroupId, menuProducts);
    }

    public Menu createWithMenuGroup(final MenuGroup menuGroup) {
        return new Menu(null, name, menuPrice, menuGroup.getId(), menuProducts);
    }
}
