package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixture {

    public static Menu getMenu(final String name, final Long price, final Long menuGroupId) {
        return getMenu(name, price, menuGroupId, Collections.emptyList());
    }

    public static Menu getMenu(final String name,
                               final Long price,
                               final Long menuGroupId,
                               final List<MenuProduct> menuProducts) {
        return Menu.builder(name, BigDecimal.valueOf(price), menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }
}
