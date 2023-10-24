package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

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
